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

  def findByMitreId(id: String): Option[StixType] = {
    findFilter(getMitreId(_) == id).headOption
  }

  def add(stixObj: StixType): Option[StixType] = {
    findByMitreId(getMitreId(stixObj)) match {
      case Some(_) => None
      case None => storage.create(stixObj).asInstanceOf[Option[StixType]]
    }
  }
}
