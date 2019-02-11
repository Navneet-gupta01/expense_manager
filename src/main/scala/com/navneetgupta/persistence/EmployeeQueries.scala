package com.navneetgupta.persistence

import com.navneetgupta.model.{Employee, EmployeeId, ExpenseSheet}
import doobie.util.update.Update0
import doobie.implicits._
import doobie.postgres.implicits._
import doobie.util.query.Query0


object EmployeeQueries {
  import DoobieCustomMapping.implicits._

  def insertQuery(employee: Employee): Update0 =
    sql"""INSERT INTO employees (id, name, surname) VALUES (${employee.id}, ${employee.name}, ${employee.surname})""".update

  def fetchQuery(id: EmployeeId): Query0[Employee] =
    sql"""SELECT * from employees where id= $id""".query[Employee]

  def countEmployeeQuery(expenseSheet: ExpenseSheet) : Query0[Long] =
    sql"""SELECT count(*) from employees where id = ${expenseSheet.employee.id}""".query[Long]

}
