package org.example.mitrestixserver
package repository

import repository.sdo._
import repository.sro._
import storage.StixStorage

trait Repositories {
  implicit def createGroupRepository(implicit storage: StixStorage): GroupRepository = new GroupRepository(storage)

  implicit def createMitigationRepository(implicit storage: StixStorage): MitigationRepository = new MitigationRepository(storage)

  implicit def createSoftwareRepository(implicit storage: StixStorage): SoftwareRepository = new SoftwareRepository(storage)

  implicit def createTacticRepository(implicit storage: StixStorage): TacticRepository = new TacticRepository(storage)

  implicit def createTechniqueRepository(implicit storage: StixStorage): TechniqueRepository = new TechniqueRepository(storage)

  implicit def createRelationshipRepository(implicit storage: StixStorage): RelationshipRepository = new RelationshipRepository(storage)
}
