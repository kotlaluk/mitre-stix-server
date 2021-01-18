package org.example.mitrestixserver
package repository.sdo

import com.kodekutters.stix.CourseOfAction


object MitigationRepository extends SDORepository[CourseOfAction] {

//  override type SDOType = CourseOfAction
  type SDOType = CourseOfAction

  override def findAll(): Seq[SDOType] = {
    storage.readAll().collect {
      case mitigation: SDOType => mitigation
    }
  }
}
