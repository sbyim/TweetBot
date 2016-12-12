package twitter.actors

import akka.actor.{Actor, Props}
import akka.actor.Actor.Receive
import akka.stream.OverflowStrategy
import akka.stream.actor.ActorPublisher
import akka.stream.scaladsl.{Keep, Sink, Source}
import twitter.model.Tweet
import twitter4j.Status

/**
  * Created by yim on 15.11.2016.
  */
object TweetStreamSubscriberActor{
  def props() : Props = Props(new TweetStreamSubscriberActor())
  case object Status
}
class TweetStreamSubscriberActor extends ActorPublisher[Status]{
  //val (actorRef, publisher) =  Source.actorRef[Tweet](1000, OverflowStrategy.fail).toMat(Sink.asPublisher(false))(Keep.both).run()
  //override def preStart = context.system.eventStream.subscribe(self, classOf[Tweet])
  override def receive: Receive = {
    case tweet : Status => {
      if (isActive && totalDemand > 0) onNext(tweet)
    }
    case _ =>
  }
  override def postStop(): Unit = {
    context.system.eventStream.unsubscribe(self)
  }

}
