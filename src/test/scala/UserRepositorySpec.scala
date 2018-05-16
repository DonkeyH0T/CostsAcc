import exppack.repository.{MemoryUserRepository, UserAuthenticationException, UserRegistrationException}
import exppack._
import exppack.domain.User
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.ExecutionContext

class UserRepositorySpec extends FlatSpec with Matchers with ScalaFutures {

  trait Context {
    implicit val ec = ExecutionContext.global
    val rep = new MemoryUserRepository
  }

  "MemoryUserRepository" should "store item" in new Context {
    rep.put(User("vasyaSPB","qwerty")).futureValue shouldBe User("vasyaSPB","qwerty",Some(1))
  }

  "MemoryUserRepository" should "return all items" in new Context {
    val f = for {
      _ <- rep.put(User("vasyaSPB", "qwerty"))
      _ <- rep.put(User("valeraSPB", "123321"))
      seq <- rep.all()
    } yield seq
    f.futureValue should contain theSameElementsAs
      Seq(User("vasyaSPB", "qwerty",Some(1)), User("valeraSPB", "123321",Some(2)))
  }

    "MemoryUserRepository" should "return new user" in new Context {
      rep.put(User("vasyaSPB","qwerty")).futureValue shouldBe User("vasyaSPB","qwerty",Some(1))
    }

  "MemoryUserRepository" should "return an exception if register the same username" in new Context {
    val f = for {
      _ <- rep.put(User("vasyaSPB", "qwerty"))
      e <- rep.put(User("vasyaSPB","qwerty"))
    } yield e
    f.failed.futureValue shouldBe a [UserRegistrationException]
  }

  "MemoryUserRepository" should "return existing user" in new Context {
    val f = for {
      _ <- rep.put(User("vasyaSPB", "qwerty"))
      user <- rep.userAuth(User("vasyaSPB","qwerty"))
    } yield user
    f.futureValue shouldBe User("vasyaSPB","qwerty",Some(1))
  }

  "MemoryUserRepository" should "return an exception if logpass is invalid" in new Context {
    val f = for {
      _ <- rep.put(User("vasyaSPB", "qwerty"))
      user <- rep.userAuth(User("vasyaSPB","123"))
    } yield user
    f.failed.futureValue shouldBe a [UserAuthenticationException]
  }

}


