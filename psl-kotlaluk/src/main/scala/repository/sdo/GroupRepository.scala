package org.example.mitrestixserver
package repository.sdo

import com.kodekutters.stix.IntrusionSet


object GroupRepository extends SDORepository {

  override type SDOType = IntrusionSet

  override def findAll(): Seq[SDOType] = {
    storage.readAll().collect {
      case group: SDOType => group
    }
  }
}
