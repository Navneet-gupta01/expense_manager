package com.navneetgupta.services

import cats.data.NonEmptyList
import com.navneetgupta.model.{EmployeeId, Expense, ExpenseSheet}

trait Services[F[_]] {
  def openExpense(employeeid: EmployeeId, expenses: NonEmptyList[Expense]): F[ExpenseSheet]
  def claimExpense(employeeid: EmployeeId, expenses: NonEmptyList[Expense]): F[ExpenseSheet]
}