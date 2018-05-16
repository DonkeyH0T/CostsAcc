import exppack.Controllers.{AddUserController, UserController}
import exppack.repository.UserRepository
import exppack.domain.{Request, User}
import exppack.domain.UserRequest.AddUser
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.{ExecutionContext, Future}

class UserControllerSpec  extends FlatSpec with Matchers with ScalaFutures with MockFactory{

  trait Context {
    implicit val ec = ExecutionContext.global
    implicit val repo = mock[UserRepository]
    val c = new AddUserController
    val uc = new UserController

  }

  "AddUserController" should "check logpass in repo and return user" in new Context {
    repo.put _ expects User("a","b") returning Future.successful(User("a","b",Some(1)))
    c(AddUser(User("a","b"))).futureValue
  }

  "UserController" should "check logpass in repo and return user" in new Context {
    repo.userAuth _ expects User("a","b") returning Future.successful(User("a","b",Some(1)))
    //req.withUser _ expects (User(1,"a","b"))
    uc(User("a","b"),Request.Remind(None)).futureValue
  }
}
