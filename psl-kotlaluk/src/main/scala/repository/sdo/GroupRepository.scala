package org.example.mitrestixserver
package repository.sdo

import storage.StixStorage

import com.kodekutters.stix.IntrusionSet


class GroupRepository(protected val storage: StixStorage) extends SDORepository[IntrusionSet] {

  type SDOType = IntrusionSet

  override def findAll(): Seq[SDOType] = {
    storage.readAll().collect {
      case group: SDOType => group
    }
  }
}
