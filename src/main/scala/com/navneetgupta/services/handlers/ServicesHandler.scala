package com.navneetgupta.services.handlers

import cats.{Monad}
import cats.data.NonEmptyList
import com.navneetgupta.model.{Employee, EmployeeId, Expense, ExpenseSheet}
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
      _ <- error.either[Employee](employee)
      openexpenseSheet <- ExpenseSheet.createOpen(employee, expenses.toList).orRaiseError
      created <- sheetRepo.save(openexpenseSheet)
    } yield created
    //ExpenseSheet.createOpen(employee, List[Expense]()).

  def claimExpense(employeeid: EmployeeId, expenses: NonEmptyList[Expense]): F[Option[ExpenseSheet]] = ???
}
//u <- error.either[AccountEntity](account.toRight(new NoSuchElementException("Invalid User Email")))