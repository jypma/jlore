package org.jlore.io.msg
import org.jlore.io.ByteBuffer

class Fixed128MessageField (val value:(Long,Long)) extends MessageField {
  import Fixed128MessageField._
  
  override def typeMarker = 6
  override def asDoubleLong:Option[(Long,Long)] = Some(value)
  override def encodeTo(buf: ByteBuffer) {
    writeLong(buf, value._1)
    writeLong(buf, value._2)
  }
}

object Fixed128MessageField {
  def apply (l1:Long, l2:Long) = new Fixed128MessageField((l1,l2))
  
  def read(buf: ByteBuffer) = {
    buf.ifRead(16) { bytes: Seq[Byte] =>
      (readLong(bytes, 0), readLong(bytes, 8))
    } map (new Fixed128MessageField(_))
  }
  
  def writeLong(buf: ByteBuffer, value: Long) {
    buf append (value & 0xFF)
    buf append ((value >> 8) & 0xFF)
    buf append ((value >> 16) & 0xFF)
    buf append ((value >> 24) & 0xFF)
    buf append ((value >> 32) & 0xFF)
    buf append ((value >> 40) & 0xFF)
    buf append ((value >> 48) & 0xFF)
    buf append ((value >> 56) & 0xFF)    
  }
  
  def readLong(bytes:Seq[Byte], offset:Int) = 
    bytes(0).toLong |
    bytes(1).toLong << 8 |
    bytes(2).toLong << 16 |
    bytes(3).toLong << 24 |
    bytes(4).toLong << 32 |
    bytes(5).toLong << 40 |
    bytes(6).toLong << 48 |
    bytes(7).toLong << 56
}