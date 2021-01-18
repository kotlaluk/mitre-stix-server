package org.example.mitrestixserver
package repository.sdo

import com.kodekutters.stix.Tool

object SoftwareRepository extends SDORepository[Tool] {

//  override type SDOType = Tool
  type SDOType = Tool

  override def findAll(): Seq[SDOType] = {
    storage.readAll().collect {
      case software: SDOType => software
    }
  }
}
