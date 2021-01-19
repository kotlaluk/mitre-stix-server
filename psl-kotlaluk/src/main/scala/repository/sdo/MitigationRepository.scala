package org.example.mitrestixserver
package repository.sdo

import storage.StixStorage

import com.kodekutters.stix.CourseOfAction


class MitigationRepository(protected val storage: StixStorage) extends SDORepository[CourseOfAction] {

  type SDOType = CourseOfAction

  override def findAll(): Seq[SDOType] = {
    storage.readAll().collect {
      case mitigation: SDOType => mitigation
    }
  }
}
