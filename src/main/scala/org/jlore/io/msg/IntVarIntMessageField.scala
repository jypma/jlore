package org.jlore.io.msg

import org.jlore.io.ByteBuffer
import VarIntMessageField._
import org.jlore.logging.Log

class IntVarIntMessageField (val int: Int) extends VarIntMessageField {
  override def asInt:Option[Int] = Some(int)
  override def encodeTo(buf: ByteBuffer) {
    val byte5 = ((int >> 28) & 0x0F)
    val byte4 = ((int >> 21) & 0x7F) 
    val byte3 = ((int >> 14) & 0x7F)
    val byte2 = ((int >> 7)  & 0x7F)
    val byte1 = ( int        & 0x7F)
    if (byte5 != 0) {
      buf << (byte1 | MSB) << (byte2 | MSB) << (byte3 | MSB) << (byte4 | MSB) << byte5
    } else if (byte4 != 0) {
      buf << (byte1 | MSB) << (byte2 | MSB) << (byte3 | MSB) << byte4 
    } else if (byte3 != 0) {
      buf << (byte1 | MSB) << (byte2 | MSB) << byte3 
    } else if (byte2 != 0) {
      buf << (byte1 | MSB) << byte2  
    } else {
      buf << byte1
    }
  }
}

object IntVarIntMessageField extends Log {
  def apply(bytes: Seq[Byte]) = {
    log.debug("reading " + bytes.length)
    new IntVarIntMessageField(bytes.length match {
      case 5 => (bytes(0) & clearMSB) |
                (bytes(1) & clearMSB) << 7  |
                (bytes(2) & clearMSB) << 14 |
                (bytes(3) & clearMSB) << 21 |
                 bytes(4)             << 28
      case 4 => (bytes(0) & clearMSB) |
                (bytes(1) & clearMSB) << 7  |
                (bytes(2) & clearMSB) << 14 |
                 bytes(3)             << 21 
      case 3 => (bytes(0) & clearMSB) |
                (bytes(1) & clearMSB) << 7  | 
                 bytes(2)             << 14
      case 2 => (bytes(0) & clearMSB) | 
                 bytes(1) << 7
      case 1 =>  bytes(0) 
    })
  }
}