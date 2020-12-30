package org.example.mitrestixserver
package dao

import com.kodekutters.stix.StixObj

trait StixObjDao {

  def findAll(removeRevoked: Boolean): Seq[StixObj]

}
