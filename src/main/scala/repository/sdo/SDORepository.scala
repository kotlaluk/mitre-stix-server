package org.example.mitrestixserver
package repository.sdo

import repository._

import com.kodekutters.stix.SDO
import play.api.libs.json._

trait SDORepository extends MitreRepository {

  type SDOType <: SDO

  private def getMitreId(sdo: SDOType): String = sdo.external_references.get(0).external_id.getOrElse("")

  def getCustomProperty[T](sdo: SDOType, property: String, reads: Reads[T]): Option[T] = {
//    val reads = Json.using[Json.WithDefaultValues].reads[T]
    sdo.custom match {
      case Some(customProps) => customProps.nodes(property).asOpt[T](reads)
      case _ => None
    }
  }

  def findAll(): Seq[SDOType]

  def findAllCurrent(): Seq[SDOType] = {
    val filter: SDOType => Boolean = sdo => {
      val customProps = sdo.custom.get
      !(sdo.revoked.getOrElse(false) || customProps.nodes.contains("x_mitre_deprecated"))
    }
    findAll().filter(filter)
  }

  def findByMitreId(id: String): Either[MitreError, SDOType] = {
    findAll().find(getMitreId(_) == id) match {
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
