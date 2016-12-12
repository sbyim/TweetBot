package twitter.extensions

import akka.actor.{ExtendedActorSystem, Extension, ExtensionId}
import com.typesafe.config.Config

/**
  * Created by yim on 02.12.2016.
  */
class KafkaSettings(config: Config) extends Extension {

  object application {
    private val applicationConfig = config.getConfig("application")
    val name = applicationConfig.getString("name")
  }

  object kafka {
    val kafkaConfig = config.getConfig("akka.kafka.producer")

    object producer {
      private val producerConfig = kafkaConfig.getConfig("producer")
      val bootstrapServers = producerConfig.getString("bootstrap.servers")
      val acks = producerConfig.getString("acks")
      val retries = producerConfig.getInt("retries")
      val batchSize = producerConfig.getInt("batch.size")
      val lingerMs = producerConfig.getInt("linger.ms")
      val bufferMemory = producerConfig.getInt("buffer.memory")
    }

    object consumer {
      private val consumerConfig = kafkaConfig.getConfig("consumer")
      val bootstrapServers = consumerConfig.getString("bootstrap.servers")
      val groupId = consumerConfig.getString("group.id")
      val topics = consumerConfig.getStringList("topics").toArray
    }
    def getProducerSettings() : Config = kafkaConfig
  }

}

object KafkaSettings extends ExtensionId[KafkaSettings] {
  override def createExtension(system: ExtendedActorSystem) = new KafkaSettings(system.settings.config)

}