package twitter

/**
  * Created by yim on 20.10.2016.
  */
import java.util.Base64

import twitter4j.TwitterFactory
import twitter4j.Twitter
import twitter4j.conf.ConfigurationBuilder

object TwitterClient {

  def main(args : Array[String]) {


    // (1) config work to create a twitter object
    val cb = new ConfigurationBuilder()
    cb.setDebugEnabled(true)
      .setOAuthConsumerKey("INMHtwy5w9EhLNTA2LT3I5nIP")
      .setOAuthConsumerSecret("sO4dKkGymkIReXY0moSRQU5ZxI50EIPhDrlry1zwf5eHBufzwN")
      .setOAuthAccessToken("2461453441-SFXB6U8AsbOXQ5PGiZfAsGSibgsqStLcRPRWS7x")
      .setOAuthAccessTokenSecret("9FFLj0zLgSy440o6P5IGSkrXeeWvCDMtcdLXppP082zYn")
    val tf = new TwitterFactory(cb.build())
    val twitter = tf.getInstance()

    // (2) use the twitter object to get your friend's timeline
    val statuses = twitter.getUserTimeline()
    System.out.println("Showing friends timeline.")
    val it = statuses.iterator()
    while (it.hasNext()) {
      val status = it.next()
      println(status.getUser().getName() + ":" +
        status.getText());
    }

  }
}