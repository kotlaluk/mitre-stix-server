package org.example.mitrestixserver
package repository

sealed trait StixError {
  val message: String
}

case class ConflictError(override val message: String = "Object with the same ID or MITRE ID already exists")
  extends StixError

case class NotFoundError(override val message: String = "Object with the specified MITRE ID does not exist")
  extends StixError

case class MismatchError(override val message: String = "MITRE ID mismatch between the request and the provided object")
  extends StixError