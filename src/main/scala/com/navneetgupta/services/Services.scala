package com.navneetgupta.services

import cats.data.NonEmptyList
import com.navneetgupta.model.{EmployeeId, Expense, ExpenseSheet}
import freestyle.tagless.tagless

@tagless(true)
trait Services[F[_]] {
  def openExpense(employeeid: EmployeeId, expenses: NonEmptyList[Expense]): F[Option[ExpenseSheet]]
  def claimExpense(employeeid: EmployeeId, expenses: NonEmptyList[Expense]): F[Option[ExpenseSheet]]
}