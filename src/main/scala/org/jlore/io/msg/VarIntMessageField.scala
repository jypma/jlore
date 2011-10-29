package org.jlore.io.msg
import org.jlore.io.ByteBuffer
import org.jlore.logging.Log
import org.jlore.logging.ObjectLog

abstract class VarIntMessageField extends MessageField {
  override def typeMarker = 0
}

object VarIntMessageField extends ObjectLog {
  val MSB = Byte.MinValue      // 1000 0000 
  val clearMSB = Byte.MaxValue // 0111 1111
  
  def apply(value:Int) = { new IntVarIntMessageField(value) }
  def apply(value:Long) = { new LongVarIntMessageField(value) }
  
  def read(buf: ByteBuffer) = {
    buf.readUntil { b:Byte => (b & MSB) == 0 }.map { bytes =>
      _log.debug("Reading: " + bytes.mkString(","))
      if (bytes.length < 5 || (bytes(4) & 0xF0) == 0) {
        IntVarIntMessageField(bytes)
      } else if (bytes.length < 10 || (bytes(9) & 0xF7) == 0) {
        LongVarIntMessageField(bytes)
      } else {
        throw new UnsupportedOperationException()
      }
    }
  }
}