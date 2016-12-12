package twitter.actors

import akka.actor.{Actor, ActorLogging}
import akka.actor.Actor.Receive
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.ByteString
import akka.http.scaladsl.marshallers.sprayjson
import akka.http.scaladsl.unmarshalling.Unmarshal
import spray.json._
import twitter.model.TestObject

import scala.concurrent.Future

/**
  * Created by yim on 28.10.2016.
  */
class TransportActor extends Actor with ActorLogging{
  import context.dispatcher
  import akka.pattern.pipe
  import twitter.model.TestObjectJsonProtocol._
  final implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))

  val httpRequest = HttpRequest(uri = "https://jsonplaceholder.typicode.com/posts/1")
  val http = Http(context.system)

  def receive: Receive ={
    case query: String =>
      val future: Future[HttpResponse] = http.singleRequest(httpRequest)
      future pipeTo self
    case HttpResponse(StatusCodes.OK,headers, entity,_)  =>
      var i = 0
      entity.dataBytes.runFold(ByteString(""))(_ ++ _).foreach { body =>
        val result = body.utf8String
        val resultAst = result.parseJson
        log.info("JSON: "+result.parseJson)
        log.info("JSON Pretty: "+result)
        log.info("TestObj" + resultAst.convertTo[TestObject])

      }

    case resp @ HttpResponse(code, _, _, _) =>
      log.info("Request failed, response code: " + code)
      resp.discardEntityBytes()

   }

}
