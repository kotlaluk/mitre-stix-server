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
    findAll().filter(sdo => {
      val isRevoked = sdo.revoked.getOrElse(false)
      val isDeprecated = sdo.getCustomProperty[Boolean]("x_mitre_deprecated").getOrElse(false)
      !(isRevoked || isDeprecated)
    })
  }

  def findById(id: Identifier): Either[MitreError, SDOType] =
    findAll().find(_.id == id).toEither(new NotFoundError)

  def findByMitreId(id: String): Either[MitreError, SDOType] =
    findAll().find(_.mitreId == id).toEither(new NotFoundError)

  def add(sdo: SDOType): Either[MitreError, SDOType] = {
    for {
      _ <- findByMitreId(sdo.mitreId).map(_ => new ConflictError).invert
      obj <- storage.create(sdo).toEither(new ConflictError)
    } yield obj.asInstanceOf[SDOType]
  }

  def update(id: String, sdo: SDOType): Either[MitreError, SDOType] = {
    if (id != sdo.mitreId)
      return Left(new MismatchError)
    for {
      _ <- findByMitreId(id)
      obj <- storage.update(sdo).toEither(new NotFoundError)
    } yield obj.asInstanceOf[SDOType]
  }

  def delete(id: String): Either[MitreError, SDOType] = {
    for {
      sdo <- findByMitreId(id)
      obj <- storage.delete(sdo).toEither(new NotFoundError)
    } yield obj.asInstanceOf[SDOType]
  }
}
