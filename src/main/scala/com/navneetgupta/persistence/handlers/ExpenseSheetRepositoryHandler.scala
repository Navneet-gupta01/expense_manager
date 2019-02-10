package com.navneetgupta.persistence.handlers

import cats.Monad
import com.navneetgupta.model.{ExpenseSheet, ExpenseSheetId}
import com.navneetgupta.persistence.ExpenseSheetRepository
import doobie.util.transactor.Transactor

class ExpenseSheetRepositoryHandler[F[_]: Monad](implicit T: Transactor[F]) extends ExpenseSheetRepository[F] {
  override def get(id: ExpenseSheetId): F[Option[ExpenseSheet]] = 

  override def save(expenseSheet: ExpenseSheet): F[Option[ExpenseSheet]] = ???
}
