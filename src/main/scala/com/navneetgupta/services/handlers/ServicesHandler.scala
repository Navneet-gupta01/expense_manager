package com.navneetgupta.services.handlers

import cats.{ApplicativeError, Monad}
import cats.implicits._
import cats.data.NonEmptyList
import com.navneetgupta.model._
import com.navneetgupta.persistence.{EmployeeRepository, ExpenseSheetRepository}
import com.navneetgupta.errors.ErrorManagement.implicits._
import freestyle.tagless.effects.error.ErrorM
import freestyle.tagless.logging.LoggingM
import freestyle.tagless.module

@module
trait ServicesHandler[F[_]] {

  implicit val M: Monad[F]
  implicit val L: LoggingM[F]
  val error: ErrorM[F]
  val employeeRepo: EmployeeRepository[F]
  val sheetRepo: ExpenseSheetRepository[F]

  def openExpense(employeeid: EmployeeId, expenses: NonEmptyList[Expense]): F[Option[ExpenseSheet]] =
    for {
      employee <- employeeRepo.get(employeeid)
      u <- error.either[Employee](employee.toRight(EntityNotFound("Invalid Employee Id")))
      openexpenseSheet <- error.either[OpenExpenseSheet](ExpenseSheet.createOpen(employee.get, expenses.toList).toEither.leftMap(l => InvalidInputParams(l.mkString_("[", ",", "]"))))
      created <- sheetRepo.save(openexpenseSheet)
      openedExpeseSheet <- error.either[ExpenseSheet](created.toRight(new NoSuchElementException("Unable to open Expense Sheet")))
    } yield created

  def claimExpense(employeeid: EmployeeId, expenses: NonEmptyList[Expense]): F[Option[ExpenseSheet]] =
    for {
      employee <- employeeRepo.get(employeeid)
      u <- error.either[Employee](employee.toRight(EntityNotFound("Invalid Employee Id")))
      claimedExpenseSheet <- error.either[ClaimedExpenseSheet](ExpenseSheet.createClaimed(employee.get, expenses.toList).toEither.leftMap(l => InvalidInputParams(l.mkString_("[",",","]"))))
      created <- sheetRepo.save(claimedExpenseSheet)
    } yield created

  def createEmployee(name: String, surname: String): F[Option[Employee]] =
    for {
      employee <- error.either[Employee](Employee.employee(name, surname).toEither.leftMap(l => InvalidInputParams(l.mkString_("[",",","]"))))
      employeeCreated <- employeeRepo.save(employee)
    } yield employeeCreated
}


