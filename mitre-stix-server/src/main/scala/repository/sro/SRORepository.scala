package org.example.mitrestixserver
package repository.sro

import storage.StixStorage

import com.kodekutters.stix.SRO


trait SRORepository[SROType <: SRO] {

  protected def storage: StixStorage

  def findAll(): Seq[SROType]

  def findByFilter(filter: SROType => Boolean): Seq[SROType] = {
    findAll().filter(filter)
  }

}
