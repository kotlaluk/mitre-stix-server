package org.example.mitrestixserver
package repository.sdo

import storage.StixStorage

import com.kodekutters.stix.Tool


class SoftwareRepository(protected val storage: StixStorage) extends SDORepository[Tool] {

  type SDOType = Tool

  override def findAll(): Seq[SDOType] = {
    storage.readAll().collect {
      case software: SDOType => software
    }
  }
}
