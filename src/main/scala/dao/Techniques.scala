package org.example.mitrestixserver
package dao

import repository.StixRepository.{filter, typeFilter}

import com.kodekutters.stix.{AttackPattern, StixObj}


object Techniques extends StixObjDao {

  override type StixType = AttackPattern

  private val techniqueFilter = typeFilter(AttackPattern.`type`)

  def findTechniques = filter(techniqueFilter)(_)

  override def findAll(removeRevoked: Boolean): Seq[AttackPattern] = {
    val allTechniques = findTechniques(_ => true).collect {
      case technique: AttackPattern => technique
    }
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
