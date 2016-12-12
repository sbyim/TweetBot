package twitter.actors

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.kafka.ProducerSettings
import akka.stream.actor.{ActorPublisher, ActorSubscriber}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.ByteString
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}
import twitter.model.TestObject
import twitter4j.Status

import scala.concurrent.Future

/**
  * Created by yim on 01.12.2016.
  */
object KafkaActor {
  def props() : Props = Props(new KafkaActor())
  case object ByteString
}
class KafkaActor extends Actor {
  //val (actorRef, publisher) =  Source.actorRef[Tweet](1000, OverflowStrategy.fail).toMat(Sink.asPublisher(false))(Keep.both).run()
  //override def preStart = context.system.eventStream.subscribe(self, classOf[Tweet])
  override def receive: Receive = {
    case tweet : ByteString => {
      println("Kafkaactor: ".concat(tweet.decodeString("US-ASCII")))
      sender() ! "ack"
    }
    case "initialized" => sender() ! "ack"
    case _ => println("Not Handled: ")
  }
}
