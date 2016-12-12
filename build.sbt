name := "ScalaAkkaBot"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies +=
  "com.typesafe.akka" %% "akka-actor" % "2.4.14"
libraryDependencies += "org.twitter4j" % "twitter4j-core" % "4.0.5"
libraryDependencies += "org.twitter4j" % "twitter4j-stream" % "4.0.5"
libraryDependencies += "com.typesafe.akka" % "akka-stream_2.11" % "2.4.11"
// https://mvnrepository.com/artifact/com.typesafe.akka/akka-http-experimental_2.11
libraryDependencies += "com.typesafe.akka" % "akka-http-experimental_2.11" % "2.4.11"

libraryDependencies += "com.typesafe.akka" % "akka-http-spray-json-experimental_2.11" % "2.4.11"
libraryDependencies += "com.typesafe.akka" % "akka-stream-kafka_2.11" % "0.13"
libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.0.2"
// https://mvnrepository.com/artifact/org.apache.spark/spark-sql_2.11
libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % "2.0.2"
libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "2.0.2"
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-10-assembly_2.11" % "2.0.2"
libraryDependencies += "com.datastax.spark" % "spark-cassandra-connector_2.11" % "2.0.0-M3"


