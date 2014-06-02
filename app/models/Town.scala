package models

/**
 * Created by Adam on 6/1/14.
 */
case class Town(id: String, name: String, map: String, zone: Int, rank: Int = 0, is_default: Boolean = false)
