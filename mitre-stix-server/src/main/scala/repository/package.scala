package org.example.mitrestixserver

import com.kodekutters.stix.SDO
import play.api.libs.json.Reads


package object utils {

  implicit class SDOUtils(val sdo: SDO) {
    val mitreId: String = sdo.external_references.flatMap(_.headOption).flatMap(_.external_id).getOrElse("")

    def getCustomProperty[T: Reads](property: String): Option[T] = {
      sdo.custom.flatMap(_.nodes.get(property)).flatMap(_.asOpt[T](implicitly[Reads[T]]))
    }
  }

}
