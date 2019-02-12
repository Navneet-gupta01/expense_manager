package com.navneetgupta.services.handlers

import cats.{ApplicativeError, Monad}
import cats.implicits._
import cats.data.NonEmptyList
import com.navneetgupta.model._
import com.navneetgupta.persistence.{EmployeeRepository, ExpenseSheetRepository}
import com.navneetgupta.errors.ErrorManagement.implicits._
import freestyle.tagless.effects.error.ErrorM
import freestyle.tagless.logging.LoggingM

trait ServicesHandler[F[_]] {

  implicit val M: Monad[F]
  implicit val L: LoggingM[F]
  val error: ErrorM[F]
  val employeeRepo: EmployeeRepository[F]
  val sheetRepo: ExpenseSheetRepository[F]

  def openExpense(employeeid: EmployeeId, expenses: NonEmptyList[Expense]): F[Option[ExpenseSheet]] =
    for {
      employee <- employeeRepo.get(employeeid)
      u <- error.either[Employee](employee.toRight(new NoSuchElementException("Invalid Employee Id")))
      openexpenseSheet <- ExpenseSheet.createOpen(employee.get, expenses.toList)
      created <- sheetRepo.save(openexpenseSheet)
      openedExpeseSheet <- error.either[ExpenseSheet](created.toRight(new NoSuchElementException("Unable to open Expense Sheet")))
    } yield created

  def claimExpense(employeeid: EmployeeId, expenses: NonEmptyList[Expense]): F[Option[ExpenseSheet]] = ???
}
//u <- error.either[AccountEntity](account.toRight(new NoSuchElementException("Invalid User Email")))