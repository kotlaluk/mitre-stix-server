package org.example.mitrestixserver
package repository.sdo

import storage.StixStorage
import utils.SDOUtils

import com.kodekutters.stix.AttackPattern


class TechniqueRepository(protected val storage: StixStorage) extends SDORepository[AttackPattern] {

  type SDOType = AttackPattern

  override def findAll(): Seq[SDOType] = {
    storage.readAll().collect {
      case technique: SDOType => technique
    }
  }

  def findCurrentWithoutSubtechniques(): Seq[SDOType] = {
    findAllCurrent().filter(
      ! _.getCustomProperty[Boolean]("x_mitre_is_subtechnique").getOrElse(false)
    )
  }
}
