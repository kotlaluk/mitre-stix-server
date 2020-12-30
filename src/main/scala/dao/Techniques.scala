package org.example.mitrestixserver
package dao

import repository.StixRepository.{filter, typeFilter}

import com.kodekutters.stix.{AttackPattern, StixObj}


object Techniques extends StixObjDao {

  private val techniqueFilter = typeFilter(AttackPattern.`type`)

  def findTechniques: (StixObj => Boolean) => Seq[StixObj] = filter(techniqueFilter)(_)

  override def findAll(removeRevoked: Boolean = false): List[AttackPattern] = {
    val allTechniques = findTechniques(_ => true).asInstanceOf[List[AttackPattern]]
    if (removeRevoked) {
      return allTechniques.filter(attack => {
        val customProps = attack.custom.get
        ! (attack.revoked.getOrElse(false) || customProps.nodes.contains("x_mitre_deprecated"))
      })
    }
    allTechniques
  }

  def findByMitreId(id: String): Option[AttackPattern] = {
    val filter: StixObj => Boolean = _.asInstanceOf[AttackPattern].external_references.get(0)
                                     .external_id.getOrElse("") == id
    findTechniques(filter).asInstanceOf[List[AttackPattern]].headOption
  }
}
