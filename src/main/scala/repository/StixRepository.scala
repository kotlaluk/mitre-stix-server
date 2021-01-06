package org.example.mitrestixserver
package repository

import com.kodekutters.stix.SDO


trait StixRepository {

  type StixType <: SDO

  protected val storage = service.MitreService.storage

  private final val messageConflict = "Object with the same ID or MITRE ID already exists"
  private final val messageNotExists = "Object with the specified MITRE ID does not exist"
  private final val messageMismatch = "MITRE ID mismatch between the request and the provided object"

  def getMitreId(stixObj: StixType): String = stixObj.external_references.get(0).external_id.getOrElse("")

  def findAll(): Seq[StixType]

  def findFilter(filter: StixType => Boolean): Seq[StixType] = {
    findAll().filter(filter)
  }

  def findAllCurrent(): Seq[StixType] = {
    val filter: StixType => Boolean = obj => {
      val customProps = obj.custom.get
      ! (obj.revoked.getOrElse(false) || customProps.nodes.contains("x_mitre_deprecated"))
    }
    findFilter(filter)
  }

  def findByMitreId(id: String): Either[String, StixType] = {
    findFilter(getMitreId(_) == id).headOption match {
      case Some(stixObj) => Right(stixObj)
      case None => Left(messageNotExists)
    }
  }

  def add(stixObj: StixType): Either[String, StixType] = {
    findByMitreId(getMitreId(stixObj)) match {
      case Right(_) => Left(messageConflict)
      case Left(_) => storage.create(stixObj) match {
        case Some(obj) => Right(obj.asInstanceOf[StixType])
        case None => Left(messageConflict)
      }
    }
  }

  def update(id: String, stixObj: StixType): Either[String, StixType] = {
    if (id != getMitreId(stixObj))
      return Left(messageMismatch)
    findByMitreId(id) match {
      case Right(_) => storage.update(stixObj) match {
        case Some(obj) => Right(obj.asInstanceOf[StixType])
        case None => Left(messageNotExists)
      }
      case Left(message) => Left(message)
    }
  }
}
