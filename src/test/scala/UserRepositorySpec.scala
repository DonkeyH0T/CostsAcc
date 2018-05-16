import exppack._
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures
import scala.concurrent.ExecutionContext

class UserRepositorySpec extends FlatSpec with Matchers with ScalaFutures {

  trait Context {
    implicit val ec = ExecutionContext.global
    val rep = new MemoryUserRepository
  }

  "MemoryUserRepository" should "store item" in new Context {
    rep.put(User(1,"vasyaSPB","qwerty")).futureValue shouldBe()
  }

  "MemoryUserRepository" should "return all items" in new Context {
    val f = for {
      _ <- rep.put(User(1, "vasyaSPB", "qwerty"))
      _ <- rep.put(User(2, "valeraSPB", "123321"))
      seq <- rep.all()
    } yield seq
    f.futureValue should contain theSameElementsAs
      Seq(User(1, "vasyaSPB", "qwerty"), User(2, "valeraSPB", "123321"))
  }

    "MemoryUserRepository" should "return new user" in new Context {
      rep.checkAndSave("vasyaSPB","qwerty").futureValue shouldBe User(1,"vasyaSPB","qwerty")
    }

  "MemoryUserRepository" should "return an exception if register the same username" in new Context {
    val f = for {
      _ <- rep.put(User(1, "vasyaSPB", "qwerty"))
      e <- rep.checkAndSave("vasyaSPB","qwerty")
    } yield e
    f.failed.futureValue shouldBe a [UserRegistrationException]
  }

  "MemoryUserRepository" should "return existing user" in new Context {
    val f = for {
      _ <- rep.put(User(1, "vasyaSPB", "qwerty"))
      user <- rep.userAuth("vasyaSPB","qwerty")
    } yield user
    f.futureValue shouldBe User(1,"vasyaSPB","qwerty")
  }

  "MemoryUserRepository" should "return an exception if logpass is invalid" in new Context {
    val f = for {
      _ <- rep.put(User(1, "vasyaSPB", "qwerty"))
      user <- rep.userAuth("vasyaSPB","123")
    } yield user
    f.failed.futureValue shouldBe a [UserAuthenticationException]
  }

}


