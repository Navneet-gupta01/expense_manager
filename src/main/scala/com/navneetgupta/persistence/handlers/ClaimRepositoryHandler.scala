package com.navneetgupta.persistence.handlers

import cats.Monad
import com.navneetgupta.model.Claim
import com.navneetgupta.persistence.ClaimRepository
import doobie.util.transactor.Transactor
import doobie.implicits._

class ClaimRepositoryHandler[F[_]: Monad](implicit T: Transactor[F]) extends ClaimRepository.Handler[F]{

  import com.navneetgupta.persistence.ClaimQueries._
  import com.navneetgupta.persistence.DoobieCustomMapping.implicits._

  override def save(claim: Claim): F[Option[Claim]] =
    insertQuery(claim).
      withUniqueGeneratedKeys[Int]("id")
    .flatMap(fetchQuery(_).option)
    .transact(T)

}
