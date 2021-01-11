package org.example.mitrestixserver
package repository

import com.kodekutters.stix.SDO


trait SDORepository {

  type SDOType <: SDO

  protected val storage = service.MitreService.storage

  def getMitreId(sdo: SDOType): String = sdo.external_references.get(0).external_id.getOrElse("")

  def findAll(): Seq[SDOType]

  def findByFilter(filter: SDOType => Boolean): Seq[SDOType] = {
    findAll().filter(filter)
  }

  def findAllCurrent(): Seq[SDOType] = {
    val filter: SDOType => Boolean = sdo => {
      val customProps = sdo.custom.get
      ! (sdo.revoked.getOrElse(false) || customProps.nodes.contains("x_mitre_deprecated"))
    }
    findByFilter(filter)
  }

  def findByMitreId(id: String): Either[MitreError, SDOType] = {
    findByFilter(getMitreId(_) == id).headOption match {
      case Some(sdo) => Right(sdo)
      case None => Left(new NotFoundError)
    }
  }

  def add(sdo: SDOType): Either[MitreError, SDOType] = {
    findByMitreId(getMitreId(sdo)) match {
      case Right(_) => Left(new ConflictError)
      case Left(_) => storage.create(sdo) match {
        case Some(obj) => Right(obj.asInstanceOf[SDOType])
        case None => Left(new ConflictError)
      }
    }
  }

  def update(id: String, sdo: SDOType): Either[MitreError, SDOType] = {
    if (id != getMitreId(sdo))
      return Left(new MismatchError)
    findByMitreId(id) match {
      case Right(_) => storage.update(sdo) match {
        case Some(obj) => Right(obj.asInstanceOf[SDOType])
        case None => Left(new NotFoundError)
      }
      case Left(message) => Left(message)
    }
  }

  def delete(id: String): Either[MitreError, SDOType] = {
    findByMitreId(id) match {
      case Right(sdo) => storage.delete(sdo) match {
        case Some(obj) => Right(obj.asInstanceOf[SDOType])
        case None => Left(new NotFoundError)
      }
      case Left(message) => Left(message)
    }
  }
}
