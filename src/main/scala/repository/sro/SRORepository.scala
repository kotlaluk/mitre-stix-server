package org.example.mitrestixserver
package repository.sro

import repository.MitreRepository

import com.kodekutters.stix.SRO

trait SRORepository extends MitreRepository {

  type SROType <: SRO

  def findAll(): Seq[SROType]

  def findByFilter(filter: SROType => Boolean): Seq[SROType] = {
    findAll().filter(filter)
  }

}
