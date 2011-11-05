package org.jlore.io.msg
import org.jlore.io.ByteBuffer

class Fixed64MessageField (val value:Long) extends MessageField {
  override def typeMarker = 1
  override def asString = Some(value.toString)
  override def asLong:Option[Long] = Some(value)
  override def encodeTo(buf: ByteBuffer) {
    buf append (value & 0xFF)
    buf append ((value >> 8) & 0xFF)
    buf append ((value >> 16) & 0xFF)
    buf append ((value >> 24) & 0xFF)
    buf append ((value >> 32) & 0xFF)
    buf append ((value >> 40) & 0xFF)
    buf append ((value >> 48) & 0xFF)
    buf append ((value >> 56) & 0xFF)
  }
}

object Fixed64MessageField {
  def read(buf: ByteBuffer) = {
    buf.ifRead(8) { bytes: Seq[Byte] =>
      bytes(0).toLong |
      bytes(1).toLong << 8 |
      bytes(2).toLong << 16 |
      bytes(3).toLong << 24 |
      bytes(4).toLong << 32 |
      bytes(5).toLong << 40 |
      bytes(6).toLong << 48 |
      bytes(7).toLong << 56
    } map (new Fixed64MessageField(_))
  }
}