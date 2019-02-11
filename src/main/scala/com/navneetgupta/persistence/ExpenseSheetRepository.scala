package com.navneetgupta.persistence

import com.navneetgupta.model.{ExpenseSheet, ExpenseSheetId}
import freestyle.tagless.tagless

@tagless(true)
trait ExpenseSheetRepository[F[_]] {
  def get(id: ExpenseSheetId): F[Option[ExpenseSheet]]
  def save(expenseSheet: ExpenseSheet): F[Option[ExpenseSheet]]
}
