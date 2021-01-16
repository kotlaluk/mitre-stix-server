package org.example.mitrestixserver
package repository.sdo

import com.kodekutters.stix.AttackPattern
import play.api.libs.json._

object TechniqueRepository extends SDORepository {

  override type SDOType = AttackPattern

  override def findAll(): Seq[SDOType] = {
    storage.readAll().collect {
      case technique: SDOType => technique
    }
  }

  def findCurrentWithoutSub(): Seq[SDOType] = {
    findAllCurrent().filter(! getCustomProperty[Boolean](_, "x_mitre_is_subtechnique", JsPath.read[Boolean])
                              .getOrElse(false))
  }
}
