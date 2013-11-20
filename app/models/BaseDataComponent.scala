package models

/**
 * Created with IntelliJ IDEA.
 * User: Adam
 * Date: 11/19/13
 * Time: 10:06 PM
 * To change this template use File | Settings | File Templates.
 */

trait BaseDataComponent[A] {

  def add(id: String, model: A): Option[A]

  def getById(id: String): Option[A]

  def all(limit: Int): List[A]

  def update(model: A): Option[A]

  def delete(id: String): Int

}
