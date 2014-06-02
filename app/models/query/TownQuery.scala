package models.query

/**
 * Created by Adam on 6/1/14.
 */
object TownQuery {
  lazy final val selectById =
    """
      |SELECT * FROM public.town WHERE id = {id}
    """.stripMargin

  lazy final val selectByUserId =
    """
      |SELECT t.*, u.rank, u.is_default FROM public.town t
      |INNER JOIN public.user_town u ON t.id = u.town_id
      |WHERE u.user_id = {user_id}
    """.stripMargin
}
