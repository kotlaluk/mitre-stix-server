package org.example.mitrestixserver
package repository.sro

import storage.StixStorage

import com.kodekutters.stix.{Relationship, SDO}


class RelationshipRepository(protected val storage: StixStorage) extends SRORepository[Relationship] {

  type SROType = Relationship

  override def findAll(): Seq[SROType] = {
    storage.readAll().collect {
      case relationship: SROType => relationship
    }
  }

  def findBySource(relType: String)(source: SDO): Seq[SROType] =
    findByFilter(sro => sro.relationship_type == relType && sro.source_ref == source.id)

  def findByTarget(relType: String)(target: SDO): Seq[SROType] =
    findByFilter(sro => sro.relationship_type == relType && sro.target_ref == target.id)

  def findUses: SDO => Seq[SROType] = findBySource("uses")
  def findUsedBy: SDO => Seq[SROType] = findByTarget("uses")
  def findSubtechniques: SDO => Seq[SROType] = findByTarget("subtechnique-of")
  def findSubtechniqueOf: SDO => Seq[SROType] = findBySource("subtechnique-of")
  def findMitigates: SDO => Seq[SROType] = findBySource("mitigates")
  def findMitigatedBy: SDO => Seq[SROType] = findByTarget("mitigates")

}
