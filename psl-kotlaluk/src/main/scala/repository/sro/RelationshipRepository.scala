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

  def findBySource(relType: String)(source: SDO): Seq[SROType] = {
    val filter: SROType => Boolean = sro => {
      sro.relationship_type == relType && sro.source_ref == source.id
    }
    findByFilter(filter)
  }

  def findByTarget(relType: String)(target: SDO): Seq[SROType] = {
    val filter: SROType => Boolean = sro => {
      sro.relationship_type == relType && sro.target_ref == target.id
    }
    findByFilter(filter)
  }

  val findUses: SDO => Seq[SROType] = findBySource("uses")
  val findUsedBy: SDO => Seq[SROType] = findByTarget("uses")
  val findSubtechniques: SDO => Seq[SROType] = findByTarget("subtechnique-of")
  val findSubtechniqueOf: SDO => Seq[SROType] = findBySource("subtechnique-of")
  val findMitigates: SDO => Seq[SROType] = findBySource("mitigates")
  val findMitigatedBy: SDO => Seq[SROType] = findByTarget("mitigates")

}
