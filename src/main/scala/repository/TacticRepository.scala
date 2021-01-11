package org.example.mitrestixserver
package repository

import com.kodekutters.stix.CustomStix


object TacticsRepository extends StixRepository {

  override type StixType = CustomStix

  override def findAll(): Seq[StixType] = {
    storage.readAll().collect {
      case tactic: StixType if (tactic.`type` == "x-mitre-tactic") => tactic
    }
  }
}
