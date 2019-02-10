package com

import com.navneetgupta.model.{ClaimId, Employee, Expense, ExpenseSheetId}
import com.navneetgupta.persistence.ClaimQueries.ClaimType
import com.navneetgupta.persistence.ExpenseSheetQueries.ExpenseSheetType

package object navneetgupta {
  type ExpenseSheetTuple = (ExpenseSheetId, ExpenseSheetType, List[Expense], Employee)
  type ClaimTuple = (ClaimId, ClaimType, Employee, List[Expense])
}
