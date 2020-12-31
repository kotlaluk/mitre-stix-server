package org.example.mitrestixserver
package dao

import com.kodekutters.stix.StixObj

trait StixObjDao {

  type StixType <: StixObj

  def findAll(removeRevoked: Boolean = true): Seq[StixType]

}
