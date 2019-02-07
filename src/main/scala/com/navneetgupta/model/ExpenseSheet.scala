package com.navneetgupta.model

import java.util.UUID

import cats.implicits._
import com.navneetgupta.errors.ErrorManagement


final case class ExpenseSheetId(uuid: UUID) extends AnyVal

sealed trait ExpenseSheet {
  def id: ExpenseSheetId
  def employee: Employee
  def expenses: List[Expense]
}

final case class OpenExpenseSheet(id: ExpenseSheetId,
                                  employee: Employee,
                                  expenses: List[Expense]) extends ExpenseSheet

final case class ClaimedExpenseSheet(id: ExpenseSheetId,
                                     employee: Employee,
                                     expenses: List[Expense]) extends ExpenseSheet

object ExpenseSheet {

  import implicits._

  private val validateId = ErrorManagement.notNull[ExpenseSheetId]("id is null")(_)
  private val validateEmployee = ErrorManagement.notNull[Employee]("employee is null")(_)

  def createOpen(id: ExpenseSheetId, employee: Employee, expenses:List[Expense]) : ErrorManagement.Validated[OpenExpenseSheet] =
    (validateId(id), validateEmployee(employee)).mapN(OpenExpenseSheet(_, _, expenses))

  def createOpen(employee: Employee, expenses:List[Expense]) : ErrorManagement.Validated[OpenExpenseSheet] =
    createOpen(UUID.randomUUID(), employee, expenses)

  private val validateExpenses = ErrorManagement.nonEmptyList[Expense]("expenses is empty")(_)

  def createClaimed(id: ExpenseSheetId, employee: Employee, expenses:List[Expense]) : ErrorManagement.Validated[ClaimedExpenseSheet] =
    (validateId(id), validateEmployee(employee), validateExpenses(expenses))
      .mapN(ClaimedExpenseSheet)

  def createClaimed(employee: Employee, expenses:List[Expense]) : ErrorManagement.Validated[ClaimedExpenseSheet] =
    createClaimed(UUID.randomUUID(), employee, expenses)

  object implicits {
    import scala.language.implicitConversions

    implicit def uuidToExpenseSheetId(uuid: UUID) : ExpenseSheetId = ExpenseSheetId(uuid)
  }
}
