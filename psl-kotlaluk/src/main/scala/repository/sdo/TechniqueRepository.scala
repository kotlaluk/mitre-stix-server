package org.example.mitrestixserver
package repository.sdo

import utils.SDOUtils

import com.kodekutters.stix.AttackPattern
import play.api.libs.json._

object TechniqueRepository extends SDORepository[AttackPattern] {

//  override type SDOType = AttackPattern
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
