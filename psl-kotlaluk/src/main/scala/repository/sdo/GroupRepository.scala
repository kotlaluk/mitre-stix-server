package org.example.mitrestixserver
package repository.sdo

import com.kodekutters.stix.IntrusionSet


object GroupRepository extends SDORepository[IntrusionSet] {

  //  override type SDOType = IntrusionSet
  type SDOType = IntrusionSet

  override def findAll(): Seq[SDOType] = {
    storage.readAll().collect {
      case group: SDOType => group
    }
  }
}
