package exppack

import java.time.LocalDate

sealed trait Request{
  def withUser(user:User): Request
}

object Request {

  case class AddUser(name: String, pass: String) extends Request {
    def withUser(user:User): AddUser = copy(currentUser = Some(user))
  }

  case class AddExpense(name: String, pass: String, exp: Data, currentUser: Option[User]) extends Request {
    def withUser(user:User): AddExpense = copy(currentUser = Some(user))
  }

  case class WithDate(name: String, pass: String, dateFrom: LocalDate,
                      dateTo: LocalDate, currentUser: Option[User]) extends Request {
    def withUser(user:User): WithDate = copy(currentUser = Some(user))
  }

  case class WithDateShop(name: String, pass: String, dateFrom: LocalDate,
                          dateTo: LocalDate, shop: String, currentUser: Option[User]) extends Request{
    def withUser(user:User): WithDateShop = copy(currentUser = Some(user))
  }

  case class WithCategory(name: String, pass: String, dateFrom: LocalDate,
                          dateTo: LocalDate, category: String, currentUser: Option[User]) extends Request{
    def withUser(user:User): WithCategory = copy(currentUser = Some(user))
  }

  case class Remind(currentUser: Option[User]) extends Request{
    def withUser(user:User): Remind = copy(currentUser = Some(user))
  }
}

