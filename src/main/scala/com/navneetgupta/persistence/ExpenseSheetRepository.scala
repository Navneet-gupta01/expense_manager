package com.navneetgupta.persistence

import com.navneetgupta.model.{ExpenseSheet, ExpenseSheetId}

trait ExpenseSheetRepository[F[_]] {
  def get(id: ExpenseSheetId): F[Option[ExpenseSheet]]
  def save(expenseSheet: ExpenseSheet): F[Option[ExpenseSheet]]
}
