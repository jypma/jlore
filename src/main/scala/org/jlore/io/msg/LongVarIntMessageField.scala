package org.jlore.io.msg

import org.jlore.io.ByteBuffer
import VarIntMessageField._
import org.jlore.logging.Log
import scala.collection.mutable.ArrayBuffer

class LongVarIntMessageField (val value: Long) extends VarIntMessageField with Log {
  override def asLong:Option[Long] = Some(value)
  override def encodeTo(buf: ByteBuffer) {
    val bytes = new Array[Long](10)
    
    bytes(9) = ((value >> 63) & 0x01)
    bytes(8) = ((value >> 56) & 0x7F)
    bytes(7) = ((value >> 49) & 0x7F)
    bytes(6) = ((value >> 42) & 0x7F)
    bytes(5) = ((value >> 35) & 0x7F)
    bytes(4) = ((value >> 28) & 0x7F)
    bytes(3) = ((value >> 21) & 0x7F) 
    bytes(2) = ((value >> 14) & 0x7F)
    bytes(1) = ((value >> 7)  & 0x7F)
    bytes(0) = ( value        & 0x7F)
    
    log.debug("Value " + value + " results in " + bytes.mkString(","))
    val length = if (value == 0) 1 else bytes.lastIndexWhere(_ != 0) + 1
    for (i <- 0 until length-1) {
      buf << (bytes(i) | MSB)
    }
    buf << bytes(length-1)
  }
}

object LongVarIntMessageField extends Log {
  def apply(bytes: Seq[Byte]) = {
    log.debug("reading " + bytes.length)
    var value:Long = 0
    var bit = 0
    for (i <- 0 until bytes.length-1) {
      value |= (bytes(i) & clearMSB).toLong << bit
      bit += 7
    }
    value |= bytes(bytes.length-1).toLong << bit
    new LongVarIntMessageField(value)
  }
}