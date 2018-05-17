package exppack.db.models

import slick.jdbc.JdbcProfile

trait DatabaseModel {
  val profile: JdbcProfile
}
