package org.example.mitrestixserver
package repository

import com.kodekutters.stix.SDO


trait StixRepository {

  type StixType <: SDO

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
    val filter: StixType => Boolean = _.external_references.get(0).external_id.getOrElse("") == id
    findFilter(filter).headOption
  }
}
