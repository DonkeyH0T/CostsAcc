import exppack.Request._
import exppack._
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
    repo.checkAndSave _ expects ("a","b") returning Future.successful(User(1,"a","b"))
    c(AddUser("a","b",None)).futureValue
  }

  "UserController" should "check logpass in repo and return user" in new Context {
    repo.userAuth _ expects ("a","b") returning Future.successful(User(1,"a","b"))
    //req.withUser _ expects (User(1,"a","b"))
    uc("a","b",Request.Remind(None)).futureValue
  }
}
