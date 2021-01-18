package org.example.mitrestixserver

import com.kodekutters.stix.SDO
import play.api.libs.json.Reads


package object utils {

  implicit class SDOUtils(val sdo: SDO) {
    val mitreId: String = sdo.external_references match {
      case Some(ref) => ref.head.external_id.getOrElse("")
      case None => ""
    }

    def getCustomProperty[T](property: String, reads: Reads[T]): Option[T] = {
//      val reads = Json.using[Json.WithDefaultValues].reads[T]
      sdo.custom match {
        case Some(customProps) => customProps.nodes.get(property) match {
          case Some(value) => value.asOpt[T](reads)
          case _ => None
        }
        case _ => None
      }
    }
  }

}
