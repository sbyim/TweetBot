package twitter.actors
import akka.actor.{Actor, Props}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import com.datastax.spark.connector._

object SparkActor {
  def props() : Props = Props(new SparkActor())
}
class SparkActor extends Actor {



  val conf = new SparkConf()
    .setMaster("local[2]")
    .setAppName("NetworkWordCount")
    .set("spark.cassandra.connection.host", "localhost")

  val sc = new SparkContext(conf)
  val ssc = new StreamingContext(conf, Seconds(1))

  val data = sc.cassandraTable("test", "test")

  val kafkaParams = Map[String,Object](
    "metadata.broker.list"->"localhost:9092",
    "bootstrap.servers"->"localhost:9092",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> "tweetGroup",
    "auto.offset.reset" -> "latest",
    "enable.auto.commit" -> (false: java.lang.Boolean)
  )
  val topics = Array("my-topic")
  val stream = KafkaUtils.createDirectStream[String, String](
    ssc,
    PreferConsistent,
    Subscribe[String, String](topics, kafkaParams)
  )

  override def receive: Receive = {
    case "start" => {
      stream.map(record=>(record.value().toString)).print
      ssc.start()
      ssc.awaitTermination()
    }
    case _ =>
  }
}