package com.navneetgupta.persistence

import com.navneetgupta.ExpenseSheetTuple
import com.navneetgupta.model._
import doobie.implicits._
import doobie.util.query.Query0
import doobie.util.update.Update0


object ExpenseSheetQueries {

  type ExpenseSheetType = String

  def insert(expenseSheet: ExpenseSheet): Update0 =
    sql"""INSERT INTO expensesheets (id, type, employeeid, expenses)
         VALUES (${expenseSheet.id}, ${expenseSheetType(expenseSheet)}, ${expenseSheet.employee.id}, ${expenseSheet.expenses})""".update


  def update(expenseSheet: ExpenseSheet): Update0 =
    sql"""UPDATE expensesheets set type= ${expenseSheetType(expenseSheet)}, employeeid = ${expenseSheet.employee.id},
         expenses = ${expenseSheet.expenses} where id = ${expenseSheet.id}
       """.update

  def count(expenseSheet: ExpenseSheet): Query0[Long] =
    sql"""SELECT count(*) from expensesheets where id = ${expenseSheet.id}""".query[Long]

  def expenseSheetType(expenseSheet: ExpenseSheet): ExpenseSheetType = expenseSheet match {
    case OpenExpenseSheet(_, _, _)  => "O"
    case _                          => "C"
  }

//  (ExpenseSheetId, ExpenseSheetType, List[Expense], Employee)

  def fetch(id: ExpenseSheetId): Query0[ExpenseSheetTuple] =
    sql"""SELECT es.id , es.type, es.expenses, em.id, em.name, em.surname from expensesheets
          es join employees em on es.employeeid = em.id where es.id = $id
       """.query[ExpenseSheetTuple]

}
