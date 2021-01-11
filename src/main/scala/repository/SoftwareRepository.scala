package org.example.mitrestixserver
package repository

import com.kodekutters.stix.Tool


object SoftwareRepository extends StixRepository {

  override type StixType = Tool

  override def findAll(): Seq[StixType] = {
    storage.readAll().collect {
      case software: StixType => software
    }
  }
}
