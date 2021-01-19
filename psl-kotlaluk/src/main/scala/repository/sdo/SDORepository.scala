package org.example.mitrestixserver
package repository.sdo

import repository._
import repository.implicits._
import storage.StixStorage
import utils.SDOUtils

import com.kodekutters.stix.{Identifier, SDO}


trait SDORepository[SDOType <: SDO] {

  protected def storage: StixStorage

  def findAll(): Seq[SDOType]

  def findAllCurrent(): Seq[SDOType] = {
    val filter: SDOType => Boolean = sdo => {
      val customProps = sdo.custom.get
      !(sdo.revoked.getOrElse(false) || customProps.nodes.contains("x_mitre_deprecated"))
    }
    findAll().filter(filter)
  }

  def findById(id: Identifier): Either[MitreError, SDOType] = {
    findAll().find(_.id == id).toEither(new NotFoundError)
  }

  def findByMitreId(id: String): Either[MitreError, SDOType] = {
    findAll().find(_.mitreId == id) match {
      case Some(sdo) => Right(sdo)
      case None => Left(new NotFoundError)
    }
  }

  def add(sdo: SDOType): Either[MitreError, SDOType] = {
    for {
      _ <- findByMitreId(sdo.mitreId).map(_ => new ConflictError).invert
      obj <- storage.create(sdo).toEither(new ConflictError)
    } yield obj.asInstanceOf[SDOType]
  }

  def update(id: String, sdo: SDOType): Either[MitreError, SDOType] = {
    if (id != sdo.mitreId)
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
