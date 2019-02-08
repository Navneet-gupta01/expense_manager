package com.navneetgupta.services.handlers

import cats.Monad
import cats.data.NonEmptyList
import com.navneetgupta.model.{EmployeeId, Expense, ExpenseSheet}
import com.navneetgupta.services.Services

class ServicesHandler[F[_]: Monad] extends Services[F] {
  override def openExpense(employeeid: EmployeeId, expenses: NonEmptyList[Expense]): F[ExpenseSheet] = ???

  override def claimExpense(employeeid: EmployeeId, expenses: NonEmptyList[Expense]): F[ExpenseSheet] = ???
}
