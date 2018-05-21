package exppack.db.models

import java.sql.Timestamp
import exppack.domain.Data
import org.joda.time.DateTime

trait CostsModel extends DatabaseModel with UserModel {

  import profile.api._


  implicit val jodaTimeMapping = MappedColumnType.base[DateTime, Timestamp](
    dt => new Timestamp(dt.getMillis()),
    ts => new DateTime(ts.getTime())
  )

  class Costs(tag: Tag) extends Table[Data](tag, "costs") {


    def date = column[DateTime]("DATE")

    def cost = column[BigDecimal]("COST")

    def category = column[Option[String]]("CATEGORY")

    def shop = column[Option[String]]("SHOP")

    def nextPayment = column[Option[DateTime]]("NEXTPAYMENT")

    def id = column[Option[Int]]("ID", O.PrimaryKey, O.AutoInc)

    def userId = column[Option[Int]]("USERID")


    override def * = (date, cost, category, shop, nextPayment, id, userId) <> (Data.tupled, Data.unapply)

    def user = foreignKey("User_FK", userId, users)(_.id)
  }

  val costs = TableQuery[Costs]


}



