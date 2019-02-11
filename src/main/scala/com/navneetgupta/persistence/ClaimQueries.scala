package com.navneetgupta.persistence

import com.navneetgupta.ClaimTuple
import com.navneetgupta.model._
import doobie.implicits._
import doobie.postgres.implicits._
import doobie.util.query.Query0
import doobie.util.update.Update0


object ClaimQueries {
  type ClaimType = String

  import DoobieCustomMapping.implicits._

  def insertQuery(claim: Claim): Update0 =
    sql"""INSERT into claims (id, type, employeeid, expenses)
            values (${claim.id}, ${claimType(claim)}, ${claim.employee.id}, ${claim.expenses.toList})""".update
//
  // (ClaimId, ClaimType, Employee, List[Expense])
  def fetchQuery(claimId: ClaimId): Query0[ClaimTuple] =
    sql"""SELECT c.id, c.type, e.id, e.name, e.surname, c.expenses from claims c
         join employees e on e.id=c.employeeid
         where c.id=$claimId""".query[ClaimTuple]

  private def claimType(claim: Claim) : ClaimType = claim match {
    case PendingClaim(_, _, _) => "P"
    case _ => "U"
  }

}
