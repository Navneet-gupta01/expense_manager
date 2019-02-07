package com.navneetgupta.persistence

import com.navneetgupta.model.{ExpenseSheet, OpenExpenseSheet}
import doobie.implicits._
import doobie.util.update.Update0


object ExpenseSheetQueries {

  type ExpenseSheetType = String

  def insert(expenseSheet: ExpenseSheet): Update0 =
    sql"""INSERT INTO expensesheets (id, type, employeeid, expenses)
         VALUES (${expenseSheet.id}, ${expenseSheetType(expenseSheet)}, ${expenseSheet.employee.id}, ${expenseSheet.expenses.toList})""".update


  def expenseSheetType(expenseSheet: ExpenseSheet): ExpenseSheetType = expenseSheet match {
    case OpenExpenseSheet(_, _, _)  => "O"
    case _                          => "C"
  }
}
