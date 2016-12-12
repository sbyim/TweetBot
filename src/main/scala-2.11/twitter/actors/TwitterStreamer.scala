package twitter.actors

import com.typesafe.config.ConfigFactory

/**
  * Created by yim on 31.10.2016.
  */
object TwitterStreamer {

}
object Util {
  val config = ConfigFactory.load()
  val appKey: String = config.getString("appKey")
  val appSecret: String = config.getString("appSecret")
  val accessToken: String = config.getString("accessToken")
  val accessTokenSecret: String = config.getString("accessTokenSecret")
}