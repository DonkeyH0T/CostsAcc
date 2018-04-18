package testdb

import java.time.LocalDate
import java.util.Scanner

case class Expense(date: LocalDate,
                   cost: Double,
                   name: Option[String]=None,
                   shop: Option[String] =None,
                   payday: Option[Int]= None,
                   payPeriod: Option[Int] = None)
object ExpsParser{

  //разбирает регулярную покупку  "2018-04-15 4000 коммунальные_платежи 10 20"
  val regPatternLong = "^(\\d\\d\\d\\d-\\d\\d-\\d\\d) (\\d*\\.?\\d?\\d?) (\\w+) (\\d\\d?) (\\d\\d?)$".r
  //разбирает регулярную покупку  "2018-04-15 100 интернет 4"
  val regPatternShort = "^(\\d\\d\\d\\d-\\d\\d-\\d\\d) (\\d*\\.?\\d?\\d?) (\\w+) (\\d\\d?)$".r
  //разбирает текущую покупку  "2018-04-15 100 молоко Лента"
  val curPatternLong = "^(\\d\\d\\d\\d-\\d\\d-\\d\\d) (\\d*\\.?\\d?\\d?) (\\w+) (\\w+)$".r
  //разбирает текущую покупку  "2018-04-15 100"
  val curPatternShort = "^(\\d\\d\\d\\d-\\d\\d-\\d\\d) (\\d*\\.?\\d?\\d?)$".r

  def parseExpense: Expense = {
    val scanner = new Scanner(System.in)
    scanner.nextLine() match {
      case curPatternShort(date,cost) => Expense(LocalDate.parse(date),cost.toDouble)
      case curPatternLong(date,cost,name,shop) => Expense(LocalDate.parse(date),cost.toDouble,Some(name),Some(shop))
      case regPatternShort(date,cost,name,pday)=> Expense(LocalDate.parse(date),cost.toDouble,Some(name),None,Some(pday.toInt))
      case regPatternLong(date,cost,name,d,p) => Expense(LocalDate.parse(date),cost.toDouble,Some(name),None,Some(d.toInt),Some(p.toInt))
      case _ => println("Incorrect data. Try again!"); parseExpense
    }
  }
}