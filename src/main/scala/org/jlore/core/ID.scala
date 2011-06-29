package org.jlore.core

import java.util.UUID

case class ID private (msb:Long, lsb: Long) {
  
}

object ID {
  def random() = {
    val id = UUID.randomUUID()
    new ID (msb=id.getMostSignificantBits(), id.getLeastSignificantBits())
  }
}