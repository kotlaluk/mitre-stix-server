package org.example.mitrestixserver
package repository.sdo

import com.kodekutters.stix.CustomStix

object TacticRepository extends SDORepository {

  override type SDOType = CustomStix

  override def findAll(): Seq[SDOType] = {
    storage.readAll().collect {
      case tactic: SDOType if (tactic.`type` == "x-mitre-tactic") => tactic
    }
  }
}
