package exppack.domain

import org.joda.time.DateTime


trait Expense {
  def withUser(user: User): Expense
}

case class Data(date: DateTime,
                cost: BigDecimal,
                category: Option[String] = None,
                shop: Option[String] = None,
                nextPayment: Option[DateTime] = None,
                id: Option[Int] = None,
                userId: Option[Int] = None) extends Expense {

  override def withUser(user: User): Data = copy(userId = user.id)

}

case class Sample(yearMonth: String, category: Option[String], sum: BigDecimal)

case class RegSample(data: DateTime, category: String, sum:  BigDecimal)