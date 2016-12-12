package twitter.model
import spray.json.{DefaultJsonProtocol, DeserializationException, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}
/**
  * Created by yim on 31.10.2016.
  */
class TestObject(val userId : Int,val id: Int,val title: String,val body: String) {

}
object TestObjectJsonProtocol extends DefaultJsonProtocol {
  implicit object TestObjectJsonProtocol extends RootJsonFormat[TestObject] {
    def write(c: TestObject) = JsObject(
      "userId" -> JsNumber(c.userId),
      "id" -> JsNumber(c.id),
      "title" -> JsString(c.title),
      "body" -> JsString(c.body)
    )
    def read(value: JsValue) = {
      value.asJsObject.getFields("userId", "id", "title", "body") match {
        case Seq(JsNumber(userId), JsNumber(id), JsString(title), JsString(body)) =>
          new TestObject(userId.toInt, id.toInt, title, body)
        case _ => throw new DeserializationException("Color expected")
      }
    }
  }
}
