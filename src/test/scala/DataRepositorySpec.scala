import exppack.repository.MemoryDataRepository
import exppack._
import exppack.domain.{Data, RegSample, Sample, User}
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures
import org.joda.time.{DateTime, LocalDate}

import scala.concurrent.ExecutionContext


class DataRepositorySpec extends FlatSpec with Matchers with ScalaFutures {

  trait Context {
    implicit val ec = ExecutionContext.global
    val rep = new MemoryDataRepository
  }

  "MemoryDataRepository" should "store item" in new Context {
    val date = DateTime.now()
    rep.put(Data(date, 100, Some("food"), Some("Prisma"))).futureValue shouldBe
      Data( date ,100,Some("food"),Some("Prisma"),None,Some(1),None)
  }

  "MemoryDataRepository" should "return all items" in new Context {
    val f = for {
      _ <- rep.put(Data(new DateTime("2018-04-15T00:00:00.000+03:00"), 100, Some("food"), Some("Prisma")))
      _ <- rep.put(Data(new DateTime("2018-05-15T00:00:00.000+03:00"), 400, Some("cafe")))
      seq <- rep.all()
    } yield seq
    f.futureValue should contain theSameElementsAs
      Seq(Data(new DateTime("2018-04-15T00:00:00.000+03:00"), 100, Some("food"),
        Some("Prisma"), None, Some(2), None), Data(new DateTime("2018-05-15T00:00:00.000+03:00"), 400, Some("cafe"),
        None, None, Some(3), None))
  }

  "MemoryDataRepository" should "return sum for category" in new Context {
    val f = for {
      _ <- rep.put(Data(new DateTime("2016-04-15T00:00:00.000+03:00"), 100,
        Some("food"), Some("prisma"), None, None, Some(1)))
      _ <- rep.put(Data(new DateTime("2017-04-15T00:00:00.000+03:00"), 200,
        Some("food"), Some("lenta"), None, None, Some(1)))
      _ <- rep.put(Data(new DateTime("2018-04-15T00:00:00.000+03:00"), 600,
        Some("food"), Some("okay"), None, None, Some(1)))
      sum <- rep.sumByCategory(new DateTime("2016-03-15T00:00:00.000+03:00"),
        new DateTime("2018-05-15T00:00:00.000+03:00"), "food", User("vasyaSPB", "qwerty", Some(1)))
    } yield sum
    f.futureValue shouldBe 900
  }

  "MemoryDataRepository" should "return sum for non-existing category" in new Context {
    val f = for {
      _ <- rep.put(Data(new DateTime("2016-04-15T00:00:00.000+03:00"), 100,
        Some("food"), Some("prisma"), None, None, Some(1)))
      _ <- rep.put(Data(new DateTime("2017-04-15T00:00:00.000+03:00"), 200,
        Some("food"), Some("lenta"), None, None, Some(1)))
      _ <- rep.put(Data(new DateTime("2018-04-15T00:00:00.000+03:00"), 600,
        Some("food"), Some("okay"), None, None, Some(1)))
      sum <- rep.sumByCategory(new DateTime("2016-03-15T00:00:00.000+03:00"),
        new DateTime("2018-05-15T00:00:00.000+03:00"), "cinema", User("vasyaSPB", "qwerty",Some(1)))
    } yield sum
    f.futureValue shouldBe 0
  }

  "MemoryDataRepository" should "return sum by shop" in new Context {
    val f = for {
      _ <- rep.put(Data(new DateTime("2016-04-15T00:00:00.000+03:00"), 100,
        Some("food"), Some("prisma"), None, None, Some(1)))
      _ <- rep.put(Data(new DateTime("2017-04-15T00:00:00.000+03:00"), 200,
        Some("food"), Some("lenta"), None, None, Some(1)))
      _ <- rep.put(Data(new DateTime("2018-04-15T00:00:00.000+03:00"), 600,
        Some("food"), Some("prisma"), None, None, Some(1)))
      sum <- rep.sumByShop(new DateTime("2016-03-15T00:00:00.000+03:00"),
        new DateTime("2018-05-15T00:00:00.000+03:00"), "prisma", User("vasyaSPB", "qwerty",Some(1)))
    } yield sum
    f.futureValue shouldBe 700
  }

  "MemoryDataRepository" should "return seq of Samples" in new Context {
    val f = for {
      _ <- rep.put(Data(new DateTime("2017-04-15T00:00:00.000+03:00"),
        200, Some("food"), Some("lenta"), None, None, Some(1)))
      _ <- rep.put(Data(new DateTime("2017-04-13T00:00:00.000+03:00"),
        200, Some("food"), Some("lenta"), None, None, Some(1)))
      _ <- rep.put(Data(new DateTime("2018-03-15T00:00:00.000+03:00"),
        300, Some("internet"), Some("prisma"), None, None, Some(1)))
      _ <- rep.put(Data(new DateTime("2018-04-15T00:00:00.000+03:00"),
        300, Some("internet"), Some("prisma"), None, None, Some(1)))
      sum <- rep.statByDate(new DateTime("2017-03-15T00:00:00.000+03:00"),
        new DateTime("2018-04-15T00:00:00.000+03:00"), User("vasyaSPB", "qwerty", Some(1)))
    } yield sum
    f.futureValue should contain theSameElementsAs
      Seq(Sample("2017-04", Some("food"), 400), Sample("2018-03", Some("internet"), 300),
        Sample("2018-04", Some("internet"), 300))
  }

  "MemoryDataRepository" should "return empty seq of Samples" in new Context {
    val f = for {
      _ <- rep.put(Data(new DateTime("2017-04-15T00:00:00.000+03:00"),
        200, Some("food"), Some("lenta"), None, None, Some(1)))
      _ <- rep.put(Data(new DateTime("2017-04-13T00:00:00.000+03:00"),
        200, Some("food"), Some("lenta"), None, None, Some(1)))
      _ <- rep.put(Data(new DateTime("2018-03-15T00:00:00.000+03:00"),
        300, Some("internet"), Some("prisma"), None, None, Some(1)))
      _ <- rep.put(Data(new DateTime("2018-04-15T00:00:00.000+03:00"),
        300, Some("internet"), Some("prisma"), None, None, Some(1)))
      sum <- rep.statByDate(new DateTime("2016-03-15T00:00:00.000+03:00"),
        new DateTime("2016-04-15T00:00:00.000+03:00"), User("vasyaSPB", "qwerty", Some(1)))
    } yield sum
    f.futureValue shouldBe empty
  }


  "MemoryDataRepository" should "return seq of RegSamples" in new Context {
    val f = for {
      _ <- rep.put(Data(new DateTime("2017-04-15T00:00:00.000+03:00"), 300,
        Some("internet"), None, Some(LocalDate.now().toDateTimeAtStartOfDay().plusDays(5)), None, Some(1)))
      _ <- rep.put(Data(new DateTime("2017-04-15T00:00:00.000+03:00"), 250,
        Some("mobile"), None, Some(LocalDate.now().toDateTimeAtStartOfDay().minusDays(10)), None, Some(1)))
      _ <- rep.put(Data(new DateTime("2018-01-15T00:00:00.000+03:00"), 1000,
        Some("credit"), None, Some(LocalDate.now().toDateTimeAtStartOfDay().plusDays(6)), None, Some(1)))
      seq <- rep.getRemind(User("vasyaSPB", "qwerty",Some(1)))
    } yield seq
    f.futureValue should contain theSameElementsAs
      Seq(RegSample(LocalDate.now().toDateTimeAtStartOfDay().plusDays(5), "internet", 300),
        RegSample(LocalDate.now().toDateTimeAtStartOfDay().minusDays(10), "mobile", 250))
  }

}