package org.example.mitrestixserver
package repository

import com.kodekutters.stix.AttackPattern

object TechniquesRepository extends StixRepository {

  override type StixType = AttackPattern

  import service.MitreService.storage

  override def findAll(): Seq[StixType] = {
    storage.readAll().collect {
      case technique: StixType => technique
    }
  }
}
