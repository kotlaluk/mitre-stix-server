package org.example.mitrestixserver
package dao

import repository.StixRepository.{filter, typeFilter}

import com.kodekutters.stix.{AttackPattern, StixObj}


object AttackDao extends StixDao {

  private val attackFilter = typeFilter(AttackPattern.`type`)

  def findAttacks: (StixObj => Boolean) => Seq[StixObj] = filter(attackFilter)(_)

  override def findAll(removeRevoked: Boolean = false): List[AttackPattern] = {
    val allAttacks = findAttacks(_ => true).asInstanceOf[List[AttackPattern]]
    if (removeRevoked) {
      return allAttacks.filter(attack => {
        val customProps = attack.custom.get
        ! (attack.revoked.getOrElse(false) || customProps.nodes.contains("x_mitre_deprecated"))
      })
    }
    allAttacks
  }

  def findByMitreId(id: String): Option[AttackPattern] = {
    val filter: StixObj => Boolean = _.asInstanceOf[AttackPattern].external_references.get(0)
                                     .external_id.getOrElse("") == id
    findAttacks(filter).asInstanceOf[List[AttackPattern]].headOption
  }
}
