package org.example.mitrestixserver
package repository

import com.kodekutters.stix.SDO


trait StixRepository {

  type StixType <: SDO

  protected val storage = service.MitreService.storage

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

  def findByMitreId(id: String): Either[StixError, StixType] = {
    findFilter(getMitreId(_) == id).headOption match {
      case Some(stixObj) => Right(stixObj)
      case None => Left(new NotFoundError)
    }
  }

  def add(stixObj: StixType): Either[StixError, StixType] = {
    findByMitreId(getMitreId(stixObj)) match {
      case Right(_) => Left(new ConflictError)
      case Left(_) => storage.create(stixObj) match {
        case Some(obj) => Right(obj.asInstanceOf[StixType])
        case None => Left(new ConflictError)
      }
    }
  }

  def update(id: String, stixObj: StixType): Either[StixError, StixType] = {
    if (id != getMitreId(stixObj))
      return Left(new MismatchError)
    findByMitreId(id) match {
      case Right(_) => storage.update(stixObj) match {
        case Some(obj) => Right(obj.asInstanceOf[StixType])
        case None => Left(new NotFoundError)
      }
      case Left(message) => Left(message)
    }
  }
}
