package org.example.mitrestixserver
package dao

import repository.StixRepository.{filter, typeFilter}

import com.kodekutters.stix.{CustomStix, StixObj}


object Tactics extends StixObjDao {

  override type StixType = CustomStix

  private val tacticFilter = typeFilter("x-mitre-tactic")

  def findTactics = filter(tacticFilter)(_)

  override def findAll(removeRevoked: Boolean): List[CustomStix] = {
    val found = removeRevoked match {
      case true => {
        val filter: StixObj => Boolean =  _.asInstanceOf[CustomStix].revoked.getOrElse(false) == false
        findTactics(filter)
      }
      case false => findTactics(_ => true)
    }
    found.asInstanceOf[List[CustomStix]]
  }
}
