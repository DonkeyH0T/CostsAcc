package testdb

import java.sql.Date
import scalikejdbc._
class UserDao extends DbConnect {

  def readAll: Seq[Expense] = {
    insideReadOnly { implicit session =>
      sql"select * from exps_t".map(rs =>
        Expense(rs.date("date").toLocalDate(),
          rs.double("cost"),
          rs.stringOpt("name"),
          rs.stringOpt("shop"),
          rs.intOpt("payday"),
          rs.intOpt("payperiod"))).list.apply()
    }
  }

  def insert(exps: Expense): Unit = {
    insideLocalTx { implicit session =>
      val date = Date.valueOf(exps.date)
      sql"""insert into exps_t (date, cost, name, shop, payday, payperiod)
      values (${date}, ${exps.cost}, ${exps.name}, ${exps.shop}, ${exps.payday}, ${exps.payPeriod})""".update.apply()
    }
  }


  //  def insert // добавить в базу данных расход +
  //  def readAll // считать все из базы и вывести в консоль
  //  def readByDate// считать за интервал
  //  def readByParameter//
  //  def delete //удалить
  //  def statByDate //статистика за интервал
  //  def statByShop // статистика по магазинам
  //  def statByCategory // статистика по категориям
  //  def statByName // статистика по имени
}
