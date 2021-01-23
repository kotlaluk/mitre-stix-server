package org.example.mitrestixserver
package storage

import com.kodekutters.stix.Bundle

trait Loader {
  def load(): Bundle
}
