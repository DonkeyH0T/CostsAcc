package exppack

import java.time.LocalDate

sealed trait Request{
  def withUser(user:User): Request
}

object Request {

  case class AddUser(name: String, pass: String, currentUser: Option[User]) extends Request {
    def withUser(user:User): AddUser = copy(currentUser = Some(user))
  }

  case class AddExpense(exp: Data, currentUser: Option[User]) extends Request {
    def withUser(user:User): AddExpense = copy(currentUser = Some(user))
  }

  case class WithDate(dateFrom: LocalDate, dateTo: LocalDate, currentUser: Option[User]) extends Request {
    def withUser(user:User): WithDate = copy(currentUser = Some(user))
  }

  case class WithDateShop(dateFrom: LocalDate, dateTo: LocalDate, shop: String,
                          currentUser: Option[User]) extends Request{
    def withUser(user:User): WithDateShop = copy(currentUser = Some(user))
  }

  case class WithCategory(dateFrom: LocalDate, dateTo: LocalDate, category: String,
                          currentUser: Option[User]) extends Request{
    def withUser(user:User): WithCategory = copy(currentUser = Some(user))
  }

  case class Remind(currentUser: Option[User]) extends Request{
    def withUser(user:User): Remind = copy(currentUser = Some(user))
  }
}

