package twitter

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by yim on 07.12.2016.
  */
object SparkTestClient extends App{
  implicit val system = ActorSystem()
  implicit  val materializer = ActorMaterializer()
  val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")
  val sc = new SparkContext(conf)

  val lines = sc.textFile("data.txt")
  val lineLengths = lines.map(s => s.length)
  val totalLength = lineLengths.reduce((a, b) => a + b)

}
