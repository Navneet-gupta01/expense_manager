package com.navneetgupta.persistence.handlers

import java.util.UUID

import cats.Monad
import cats.implicits._
import com.navneetgupta.ExpenseSheetTuple
import com.navneetgupta.model._
import com.navneetgupta.persistence.ExpenseSheetRepository
import doobie.implicits._
import doobie.postgres.implicits._
import doobie.util.transactor.Transactor

class ExpenseSheetRepositoryHandler[F[_]: Monad](implicit T: Transactor[F]) extends ExpenseSheetRepository.Handler[F] {

  import com.navneetgupta.persistence.EmployeeQueries.countEmployeeQuery
  import com.navneetgupta.persistence.ExpenseSheetQueries._
  import com.navneetgupta.persistence.DoobieCustomMapping.implicits._

  override def update(expenseSheet: ExpenseSheet): F[Option[ExpenseSheet]] =
    updateQuery(expenseSheet).run.flatMap(_ => fetchQuery(expenseSheet.id).option.map(expenseSheetFromOptionalTuple)).transact(T)

  override def get(id : ExpenseSheetId): F[Option[ExpenseSheet]] =
    fetchQuery(id).option.map(a => expenseSheetFromOptionalTuple(a)).transact(T)


  override def insert(expenseSheet: ExpenseSheet): F[Option[ExpenseSheet]] =
    insertQuery(expenseSheet).withUniqueGeneratedKeys[UUID]("id").flatMap(a => fetchQuery(ExpenseSheetId(a)).option.map(b => expenseSheetFromOptionalTuple(b))).transact(T)



  private def expenseSheetType(expenseSheet: ExpenseSheet): ExpenseSheetType = expenseSheet match {
    case OpenExpenseSheet(_, _, _)  => "O"
    case _                          => "C"
  }

  private def expenseSheetFromTuple(expenseSheetTuple: ExpenseSheetTuple) : ExpenseSheet = {
    val (expenseSheetId, sheetType, expenses, employee) = expenseSheetTuple
    sheetType match {
      case "O" => OpenExpenseSheet(expenseSheetId, employee, expenses)
      case _ => ClaimedExpenseSheet(expenseSheetId,employee,expenses)
    }
  }

  private def expenseSheetFromOptionalTuple(expenseSheetTuple: Option[ExpenseSheetTuple]) =
    expenseSheetTuple.map(expenseSheetFromTuple(_))

  override def save(expenseSheet: ExpenseSheet): F[Option[ExpenseSheet]] =
    for {
      employeeCount <- countEmployeeQuery(expenseSheet).unique.transact(T)
      expenseSheetCount <- countSheetQuery(expenseSheet).unique.transact(T)
      insertedOrUpdated <-
        if(employeeCount === 0L) none[ExpenseSheet].pure[F]
        else if(expenseSheetCount===0L) insert(expenseSheet)
        else update(expenseSheet)
    } yield insertedOrUpdated

}
