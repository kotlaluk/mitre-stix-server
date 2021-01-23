package org.example.mitrestixserver
package repository.sdo

import storage.StixStorage

import com.kodekutters.stix.CustomStix


class TacticRepository(protected val storage: StixStorage) extends SDORepository[CustomStix] {

  type SDOType = CustomStix

  override def findAll(): Seq[SDOType] = {
    storage.readAll().collect {
      case tactic: SDOType if (tactic.`type` == "x-mitre-tactic") => tactic
    }
  }
}
