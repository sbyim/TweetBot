package twitter

import akka.actor.{ActorSystem, Props}
import akka.kafka.ProducerSettings
import akka.stream.ActorMaterializer
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}
import twitter.actors.SimpleStatusListenerActor.StartStream
import twitter.actors.{SimpleStatusListenerActor, SparkActor, TransportActor}

/**
  * Created by yim on 28.10.2016.
  */
object MyTwitterClient extends App{

  implicit val system = ActorSystem()
  implicit  val materializer = ActorMaterializer()

  val producerSettings = ProducerSettings(system, new ByteArraySerializer, new StringSerializer)
    .withBootstrapServers("localhost:9092")


  val twitterBot = system.actorOf(Props[SimpleStatusListenerActor],"twitterBot")
  twitterBot ! StartStream

}
