package com.navneetgupta.persistence.handlers

import java.util.UUID

import cats.Monad
import com.navneetgupta.ExpenseSheetTuple
import com.navneetgupta.model._
import com.navneetgupta.persistence.ExpenseSheetRepository
import doobie.implicits._
import doobie.util.transactor.Transactor

class ExpenseSheetRepositoryHandler[F[_]: Monad](implicit T: Transactor[F]) extends ExpenseSheetRepository.Handler[F] {

  import com.navneetgupta.persistence.ExpenseSheetQueries._

  override def get(id : ExpenseSheetId): F[Option[ExpenseSheet]] =
    fetchQuery(id).option.map(a => expenseSheetFromOptionalTuple(a)).transact(T)


  override def save(expenseSheet: ExpenseSheet): F[Option[ExpenseSheet]] =
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
}
