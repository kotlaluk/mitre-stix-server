package org.example.mitrestixserver
package repository.sro

import com.kodekutters.stix.{Relationship, SDO}

object RelationshipRepository extends SRORepository {

  override type SROType = Relationship

  override def findAll(): Seq[SROType] = {
    storage.readAll().collect {
      case relationship: SROType => relationship
    }
  }

  //TODO: generic function src-rel-target
  def findByType(rel_type: String)(src: SDO): Seq[SROType] = {
    val filter: SROType => Boolean = sro => {
      sro.relationship_type == rel_type && sro.source_ref == src.id
    }
    findByFilter(filter)
  }

  val findUses: SDO => Seq[SROType] = findByType("uses")

}
