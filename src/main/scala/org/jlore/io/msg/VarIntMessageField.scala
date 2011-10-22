package org.jlore.io.msg
import org.jlore.io.ByteBuffer

abstract class VarIntMessageField extends MessageField {

}

object VarIntMessageField {
  val MSB = Byte.MinValue      // 1000 0000 
  val clearMSB = Byte.MaxValue // 0111 1111
  
  def read(buf: ByteBuffer) = {
    buf.readUntil { b:Byte => (b & MSB) > 0 }.map { bytes =>
      if (bytes.length < 5 || (bytes(4) & 0xF0) == 0) {
        IntVarIntMessageField(bytes)
      } else if (bytes.length < 10 || (bytes(9) & 0xF7) == 0) {
        throw new UnsupportedOperationException()
      } else {
        throw new UnsupportedOperationException()
      }
    }
  }
}