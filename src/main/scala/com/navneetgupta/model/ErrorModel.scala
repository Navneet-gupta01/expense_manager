package com.navneetgupta.model

sealed trait AppError extends Exception
case class InvalidInputParams(msg: String) extends AppError
case class EntityNotFound(msg: String) extends AppError
