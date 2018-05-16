package exppack.domain

import org.joda.time.DateTime

sealed trait Request {
  def withUser(user: User): Request
}

object Request {

  case class AddExpense(exp: Data, currentUser: Option[User]) extends Request {
    def withUser(user: User): AddExpense = copy(currentUser = Some(user))
  }

  case class WithDate(dateFrom: DateTime, dateTo: DateTime, currentUser: Option[User]) extends Request {
    def withUser(user: User): WithDate = copy(currentUser = Some(user))
  }

  case class WithDateShop(dateFrom: DateTime, dateTo: DateTime, shop: String,
                          currentUser: Option[User]) extends Request {
    def withUser(user: User): WithDateShop = copy(currentUser = Some(user))
  }

  case class WithCategory(dateFrom: DateTime, dateTo: DateTime, category: String,
                          currentUser: Option[User]) extends Request {
    def withUser(user: User): WithCategory = copy(currentUser = Some(user))
  }

  case class Remind(currentUser: Option[User]) extends Request {
    def withUser(user: User): Remind = copy(currentUser = Some(user))
  }

}

trait UserRequest

object UserRequest {

  case class AddUser(user: User) extends UserRequest

}