package com.navneetgupta.persistence.handlers

import java.util.UUID

import cats.Monad
import cats.data.NonEmptyList
import cats.implicits._
import cats.syntax.list._
import com.navneetgupta.ClaimTuple
import com.navneetgupta.model._
import com.navneetgupta.persistence.ClaimRepository
import doobie.util.transactor.Transactor
import doobie.implicits._
import doobie.postgres.implicits._

class ClaimRepositoryHandler[F[_]: Monad](implicit T: Transactor[F]) extends ClaimRepository.Handler[F]{

  import com.navneetgupta.persistence.ClaimQueries._
  import com.navneetgupta.persistence.DoobieCustomMapping.implicits._

  override def save(claim: Claim): F[Option[Claim]] =
    insertQuery(claim).
      withUniqueGeneratedKeys[UUID]("id")
    .flatMap(a => fetchQuery(ClaimId(a)).option.map(claimFromOptionalClaimTuple(_)))
    .transact(T)

  private def claimFromOptionalClaimTuple(claimTuple: Option[ClaimTuple]): Option[Claim] =
    claimTuple.map(t => claimFromClaimTuple(t))

  private def claimFromClaimTuple(claimTuple: ClaimTuple): Claim =  {
    val (id, cType, emp, expenseList) = claimTuple
    PendingClaim(id, emp, NonEmptyList.fromList(expenseList).get)
  }

}

