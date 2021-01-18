package org.example.mitrestixserver
package repository.sdo

import com.kodekutters.stix.IntrusionSet
import org.example.mitrestixserver.storage.StixStorage


class GroupRepository(protected val storage: StixStorage) extends SDORepository2[IntrusionSet] {

  //  override type SDOType = IntrusionSet
  type SDOType = IntrusionSet

  override def findAll(): Seq[SDOType] = {
    storage.readAll().collect {
      case group: SDOType => group
    }
  }
}
