package org.example.mitrestixserver
package storage

import com.kodekutters.stix.StixObj

trait StixStorage {

  def create(stixObj: StixObj): Option[StixObj]

  def readAll(): Seq[StixObj]

  def read(filter: StixObj => Boolean): Seq[StixObj]

  def update(stixObj: StixObj): Unit

  def delete(filter: StixObj => Boolean): Unit

}
