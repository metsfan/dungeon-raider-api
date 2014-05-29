package models

/**
 * Created by Adam on 5/18/14.
 */

case class User(var id: String, auth: UserAuth, profile: UserProfile)

case class UserProfile(var id: String, username: String, email: String, first_name: String, last_name: String)

case class UserAuth(var id: String, username: String, email: String, password: String, salt: String)
