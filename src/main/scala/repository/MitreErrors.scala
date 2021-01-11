package org.example.mitrestixserver
package repository

sealed trait MitreError {
  val message: String
}

case class ConflictError(override val message: String = "Object with the same ID or MITRE ID already exists")
  extends MitreError

case class NotFoundError(override val message: String = "Object with the specified MITRE ID does not exist")
  extends MitreError

case class MismatchError(override val message: String = "MITRE ID mismatch between the request and the provided object")
  extends MitreError