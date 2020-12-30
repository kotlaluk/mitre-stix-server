package org.example.mitrestixserver
package dao

import repository.StixRepository

import com.kodekutters.stix.AttackPattern


object AttackDao extends StixDao {

  private val attackType = AttackPattern.`type`

  override def findAll(removeRevoked: Boolean = false): List[AttackPattern] = {
    val allAttacks = StixRepository.getAllByType(attackType).asInstanceOf[List[AttackPattern]]
    if (removeRevoked) {
      return allAttacks.filter(attack => {
        val customProps = attack.custom.get
        ! (attack.revoked.getOrElse(false) || customProps.nodes.contains("x_mitre_deprecated"))
      })
    }
    allAttacks
  }
}
