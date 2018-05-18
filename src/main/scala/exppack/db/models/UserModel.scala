package exppack.db.models

import exppack.domain.User


trait UserModel extends DatabaseModel {
 import profile.api._
  class Users(tag: Tag) extends Table[User](tag,"user") {
    def id = column[Option[Int]]("ID",O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def pass = column[String]("PASS")
    override def * = (name, pass,id) <> (User.tupled, User.unapply)
  }

  val users = TableQuery[Users]

}

