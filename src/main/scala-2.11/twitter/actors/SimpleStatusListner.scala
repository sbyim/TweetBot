package twitter.actors

import java.nio.file.{Path, Paths, StandardOpenOption}

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.{ActorMaterializer, IOResult}
import akka.stream.scaladsl.{FileIO, Flow, GraphDSL, Keep, RunnableGraph, Sink, Source}
import akka.util.ByteString
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}
import twitter.extensions.KafkaSettings
import twitter4j._
import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder

import scala.concurrent.Future

/**
  * Created by yim on 02.11.2016.
  */
class SimpleStatusListenerActor() extends Actor {
  import SimpleStatusListenerActor._
  import GraphDSL.Implicits._

  val settings = KafkaSettings(context.system)

  val twitterStream = new TwitterStreamFactory(new ConfigurationBuilder().build()).getInstance
  twitterStream.setOAuthConsumer(Util.appKey,Util.appSecret)
  twitterStream.setOAuthAccessToken(new AccessToken(Util.accessToken,Util.accessTokenSecret))
  val producerSettings = ProducerSettings(settings.kafka.getProducerSettings(), new ByteArraySerializer, new StringSerializer)
    .withBootstrapServers("localhost:9092")

  implicit val materializer = ActorMaterializer()(context)

  val tweetPublisherActorRef = TweetStreamSubscriberActor.props()
  val kafkaActorRef = context.actorOf(Props[KafkaActor], name = "kafkaActor")
  val sparkActor = context.actorOf(Props[SparkActor],"sparkActor")

  sparkActor ! "start"

  val tweets: Source[Status,ActorRef] = Source.actorPublisher(tweetPublisherActorRef)

  val tweetFilter =
    Flow[Status]
      .map { elem =>
        new ProducerRecord[Array[Byte], String]("my-topic", elem.getText)
      }
    /*.map(s=>ByteString(s.getText+"\n"))*/

  val sinkPrint = Flow[ProducerRecord[Array[Byte],String]]
    .to(Sink.foreach(t=>println(t.topic().concat(" key: ").concat(" value: ").concat(t.value()))))

  val sink: Sink[ByteString, Future[IOResult]] =
    FileIO.toPath(Paths.get("tweets.txt"))

  /*val actorSink : Sink[ByteString, NotUsed] = Sink.actorRefWithAck(kafkaActorRef,"initialized","ack","completed", (t:Throwable) => {t.printStackTrace()})
  val flowSink = tweetFilter.to(actorSink)*/
  val producerSink = tweetFilter.to(Producer.plainSink(producerSettings))

  val publisher = tweetFilter.to(Producer.plainSink(producerSettings)).runWith(tweets)
  //val publisher = producerSink.runWith(tweets)


  def simpleStatusListener = new StatusListener(){
    def onStatus(status: Status) {
      //println(status.getText)
      //println("received tweet: ".concat(status.getText))
      //println("sending it to actor ".concat(status.getText))
      publisher ! status

      //actorSystem.eventStream.publish(status.getText)
    }
    def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {}
    def onTrackLimitationNotice(numberOfLimitedStatuses: Int) {}
    def onException(ex: Exception) { ex.printStackTrace }
    def onScrubGeo(arg0: Long, arg1: Long) {}
    def onStallWarning(warning: StallWarning) {}
  }

  def receive = {
    case StartStream =>
      twitterStream.addListener(simpleStatusListener)
      twitterStream.filter("Software","Technology","Science")
      Thread.sleep(2000)
    case QuitStream =>
      twitterStream.cleanUp
      twitterStream.shutdown
  }
}
object SimpleStatusListenerActor {
  sealed abstract class Command
  case object StartStream
  case object QuitStream

}