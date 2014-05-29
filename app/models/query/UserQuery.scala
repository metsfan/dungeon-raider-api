package models.query

/**
 * Created by Adam on 5/25/14.
 */
object UserQuery {
  lazy final val selectUser =
    """
      |SELECT * FROM public.user
      |WHERE id = {id}
    """.stripMargin

  lazy final val selectUserByUsername =
    """
      |SELECT * FROM public.user
      |WHERE username ILIKE {username}
    """.stripMargin

  lazy final val selectUserByEmail =
    """
      |SELECT * FROM public.user
      |WHERE email ILIKE {email}
    """.stripMargin

  lazy final val selectUserAuthById =
    """
      |SELECT id, username, email, password, salt
      |FROM public.user
      |WHERE id = {id}
    """.stripMargin

  lazy final val selectUserAuthByUsername =
    """
      |SELECT id, username, email, password, salt
      |FROM public.user
      |WHERE username ILIKE {username}
    """.stripMargin

  lazy final val selectUserAuthByEmail =
    """
      |SELECT id, username, email, password, salt
      |FROM public.user
      |WHERE email ILIKE {email}
    """.stripMargin

  lazy final val insertUser =
    """
      |INSERT INTO public.user (username, email, password, salt, first_name, last_name, id) VALUES
      |({username}, {email}, {password}, {salt}, {first_name}, {last_name}, {id})
    """.stripMargin

  lazy final val updateUser =
    """
      |UPDATE public.user SET username = {username}, password = {password}, salt = {salt},
      |first_name = {first_name}, last_name = {last_name} WHERE id = {id}
    """.stripMargin
}
