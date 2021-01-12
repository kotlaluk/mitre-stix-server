package org.example.mitrestixserver
package repository.sdo

import com.kodekutters.stix.AttackPattern

object TechniqueRepository extends SDORepository {

  override type SDOType = AttackPattern

  override def findAll(): Seq[SDOType] = {
    storage.readAll().collect {
      case technique: SDOType => technique
    }
  }
}
