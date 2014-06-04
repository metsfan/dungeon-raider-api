package models

import scala.slick.driver.PostgresDriver.simple._

case class CharClass(id: Int, name: String)

class CharClasses(tag: Tag) extends Table[CharClass](tag, Some("public"), "class") {
  def id = column[Int]("id", O.PrimaryKey)
  def name = column[String]("name")

  def * = (id, name) <> (CharClass.tupled, CharClass.unapply)
}
