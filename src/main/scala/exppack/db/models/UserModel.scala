package exppack.db.models

import slick.jdbc.JdbcProfile
/*
trait UserModel {

  class Users(tag: Tag) extends Table[User](tag,"USERS") {
    def id = column[Int]("ID",O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def pass = column[String]("PASS")
    override def * = (id, name, pass) <> (User.tupled, User.unapply)
  }

  val users = TableQuery[Rooms]

}*/