package testdb

import java.util.Scanner
import scalikejdbc.config.DBs


object Utils {
  def printMenu(): Unit = println("Menu:" + "\n" +
    "1 - Add  expense" + "\n" +
    "2 - Read expense" + "\n" +
    "3 - Check statistic" + "\n" +
    "4 - накопление" + "\n" +
    "0 - quit" + "\n" +
    "Enter your choice:")
}


object CostsControlApp extends App {
  import ExpsParser._
  DBs.setup('costs_db)
  val scanner = new Scanner(System.in)
  val dao = new UserDao
  Utils.printMenu
  var closeSession = false
  while (!closeSession) {
    val userChoice = scanner.nextInt()
    scanner.nextLine()
    userChoice match {
      case 1 => {
        println("Enter expense")
        val newExp: Expense= parseExpense
       // println(newExp)
        dao.insert(newExp)
        Utils.printMenu
      }
      case 2 =>
        //чтение из базы
        dao.readAll.map(println)
        Utils.printMenu

      case 3 =>
        //просмотр статистики
      case 0 => closeSession = true
      case _ =>
    }
  }

  println("Application is closed. Bye!")
}
