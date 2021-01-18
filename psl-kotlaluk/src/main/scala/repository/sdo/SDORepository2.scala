package org.example.mitrestixserver
package repository.sdo

import repository._
import utils.SDOUtils

import com.kodekutters.stix.{Identifier, SDO, StixObj}
import org.example.mitrestixserver.storage.StixStorage

trait SDORepository2[SDOType <: SDO] {

  protected def storage: StixStorage

//  type SDOType <: SDO

  private def toSdo(stixObj: StixObj) = {
    stixObj.asInstanceOf[SDOType]
  }

  def findAll(): Seq[SDOType]

  def findAllCurrent(): Seq[SDOType] = {
    val filter: SDOType => Boolean = sdo => {
      val customProps = sdo.custom.get
      !(sdo.revoked.getOrElse(false) || customProps.nodes.contains("x_mitre_deprecated"))
    }
    findAll().filter(filter)
  }

  def findById(id: Identifier): Either[MitreError, SDOType] = {
//    findAll().find(_.id == id).fold(Left(new NotFoundError))(Right(toSdo))
    findAll().find(_.id == id) match {
      case Some(sdo) => Right(sdo.asInstanceOf[SDOType])
      case None => Left(new NotFoundError)
    }
  }

  def findByMitreId(id: String): Either[MitreError, SDOType] = {
    findAll().find(_.mitreId == id) match {
      case Some(sdo) => Right(sdo)
      case None => Left(new NotFoundError)
    }
  }

  def add(sdo: SDOType): Either[MitreError, SDOType] = {
    findByMitreId(sdo.mitreId) match {
      case Right(_) => Left(new ConflictError)
      case Left(_) => storage.create(sdo) match {
        case Some(obj) => Right(obj.asInstanceOf[SDOType])
        case None => Left(new ConflictError)
      }
    }
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
