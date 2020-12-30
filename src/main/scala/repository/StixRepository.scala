package org.example.mitrestixserver
package repository

import com.kodekutters.stix.{Bundle, StixObj}


object StixRepository {

  implicit class Predicate[A](val p1: A => Boolean) extends AnyVal {
    def and[B >: A](p2: B => Boolean): A => Boolean = (a: A) => p1(a) && p2(a)
    def or[B >: A](p2: B => Boolean): A => Boolean = (a: A) => p1(a) || p2(a)
  }

  private var stixData = new Bundle()

  def initialize(loader: Loader): Unit = {
    stixData = loader.load().get
  }

  def filter(filter1: StixObj => Boolean)(filter2: StixObj => Boolean): Seq[StixObj] = {
    stixData.objects.filter(filter1 and filter2).toList
  }

  def typeFilter(stixType: String): StixObj => Boolean = _.`type` == stixType

}



