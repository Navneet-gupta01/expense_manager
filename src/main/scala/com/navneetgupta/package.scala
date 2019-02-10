package com

import com.navneetgupta.model.{Employee, Expense, ExpenseSheetId}
import com.navneetgupta.persistence.ExpenseSheetQueries.ExpenseSheetType

package object navneetgupta {
  type ExpenseSheetTuple = (ExpenseSheetId, ExpenseSheetType, List[Expense], Employee)
}
