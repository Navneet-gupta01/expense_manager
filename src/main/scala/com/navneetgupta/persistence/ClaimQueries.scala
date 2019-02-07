package com.navneetgupta.persistence

import com.navneetgupta.model.{Claim, PendingClaim}
import doobie.implicits._
import doobie.util.update.Update0


object ClaimQueries {
  type ClaimType = String

  def insert(claim: Claim): Update0 =
    sql"""INSERT into claims (id, type, employeeid, expenses)
            values (${claim.id}, ${claimType(claim)}, ${claim.employee.id}, ${claim.expenses.toList})""".update
//
//  def get(claimId: ClaimId): Query0[(ClaimId, ClaimType, EmployeeId,  )] =
//    sql"""SELECT (id, empl)""".query[Claim]

  private def claimType(claim: Claim) : ClaimType = claim match {
    case PendingClaim(_, _, _) => "P"
    case _ => "U"
  }

}
