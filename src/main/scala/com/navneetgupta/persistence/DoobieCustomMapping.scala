package com.navneetgupta.persistence

import com.navneetgupta.model.Expense
import scala.reflect.runtime.universe.TypeTag
import doobie.util.Meta
import io.circe.{Decoder, Encoder, Json}
import org.postgresql.util.PGobject
import cats.implicits._
import io.circe.jawn._, io.circe.syntax._

object DoobieCustomMapping {

  implicit val JsonMeta: Meta[Json] =
    Meta.Advanced.other[PGobject]("json").timap[Json](
      a => parse(a.getValue).leftMap[Json](e => throw e).merge)(
      a => {
        val o = new PGobject
        o.setType("json")
        o.setValue(a.noSpaces)
        o
      }
    )

  def codecMeta[A: Encoder : Decoder : TypeTag]: Meta[A] =
    Meta[Json].timap[A](_.as[A].fold[A](throw _, identity))( _.asJson)

  object implicits {
    implicit val ExpenseListMeta: Meta[List[Expense]] = codecMeta[List[Expense]]
  }
}
