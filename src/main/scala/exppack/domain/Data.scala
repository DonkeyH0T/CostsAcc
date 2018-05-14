package exppack

import java.time.{LocalDate, Month, Year}

trait Expense{
  def withUser(user:Option[User]): Expense
}

case class Data(date: LocalDate,
                cost: Double,
                category: Option[String]=None,
                shop: Option[String] =None,
                nextPayment: Option[LocalDate]= None,
                id: Option[Int]=None,
                userId: Option[Int]=None) extends Expense {
  override def withUser(user: Option[User]): Data = user match {
    case Some(u) => copy(userId = Some(u.id))
  }
}

case class Sample(monthYear: (Month, Year), category: Option[String], sum: BigDecimal)

case class RegSample(data: LocalDate, category: String, sum: Double)