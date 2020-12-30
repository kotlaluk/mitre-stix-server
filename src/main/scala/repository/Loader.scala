package org.example.mitrestixserver
package repository

import com.kodekutters.stix.Bundle

trait Loader {
  def load(): Option[Bundle]
}
