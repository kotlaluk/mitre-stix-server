package org.example.mitrestixserver
package repository.sdo

import storage.StixStorage

trait Repositories {
  implicit def createGroupRepository(implicit storage: StixStorage): GroupRepository = new GroupRepository(storage)

}
