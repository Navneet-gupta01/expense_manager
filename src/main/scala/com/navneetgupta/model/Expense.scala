package com.navneetgupta.model

import java.util.Date

import cats.implicits._
import com.navneetgupta.errors.ErrorManagement

final case class Money(amount: Double) extends AnyVal

sealed trait Expense {
  def cost: Money
  def date: Date
}

final case class TravelExpense(cost: Money, date: Date, from: String, to: String) extends Expense
final case class FoodExpense(cost: Money, date: Date) extends Expense
final case class AccommodationExpense(cost: Money, hotel: String, date: Date) extends Expense
final case class OtherExpense(cost: Money, date: Date, description: String) extends Expense

object Expense {
  import implicits._

  def validateCost(cost: Money) : ErrorManagement.Validated[Money] = cost match {
    case Money(x) if x > 0.0 => cost.validNel
    case _ => "Invalid amount specified, amount must be > 0".invalidNel
  }

  val validateLocations = ErrorManagement.notEmptyString("Invalid From/to Location entered")(_)
  val validateDate = ErrorManagement.dateInThePastOrToday("Invalid Date specified")(_ )
  val validateHotel =  ErrorManagement.notEmptyString("Invalid Hotel details")(_)
  val validateDescription = ErrorManagement.notEmptyString("Invalid Expense Description")(_)

  def createTravelExpense(cost: Double, date: Date, from: String, to: String): ErrorManagement.Validated[TravelExpense] =
    (validateCost(cost), validateDate(date), validateLocations(from), validateLocations(to)).mapN(TravelExpense)

  def createFoodExpense(cost: Double, date: Date): ErrorManagement.Validated[FoodExpense] =
    (validateCost(cost), validateDate(date)).mapN(FoodExpense)

  def createAccommodationExpense(cost: Double, hotel: String, date: Date): ErrorManagement.Validated[AccommodationExpense] =
    (validateCost(cost), validateHotel(hotel), validateDate(date)).mapN(AccommodationExpense)

  def createOtherExpense(cost: Double, date: Date, description: String): ErrorManagement.Validated[OtherExpense] =
    (validateCost(cost),validateDate(date), validateDescription(description)).mapN(OtherExpense)

  //  def createTravelExpense(cost: Money, date: Date, from: String, to: String): ErrorManagement.Validated[TravelExpense] =
  //    (validateCost(cost), validateDate(date), validateLocations(from), validateLocations(to)).mapN(TravelExpense)

  //  def createFoodExpense(cost: Money, date: Date): ErrorManagement.Validated[FoodExpense] =
  //    (validateCost(cost), validateDate(date)).mapN(FoodExpense)


  //  def createAccommodationExpense(cost: Money, hotel: String, date: Date): ErrorManagement.Validated[AccommodationExpense] =
  //    (validateCost(cost), validateHotel(hotel), validateDate(date)).mapN(AccommodationExpense)

  //  def createOtherExpense(cost: Money, date: Date, description: String): ErrorManagement.Validated[OtherExpense] =
  //    (validateCost(cost),validateDate(date), validateDescription(description)).mapN(OtherExpense)

  object implicits {
    import scala.language.implicitConversions

    implicit def amountToMoney(amount: Double): Money = Money(amount)
  }
}