package org.example.mitrestixserver
package dao

import com.kodekutters.stix.StixObj

trait StixDao {

  def findAll(removeRevoked: Boolean): Seq[StixObj]

}
