package twitter.model

import twitter4j.User

/**
  * Created by yim on 03.11.2016.
  */
class Tweet(
             text: String,
             user: User) {
  def getTweet() : String = text
}
