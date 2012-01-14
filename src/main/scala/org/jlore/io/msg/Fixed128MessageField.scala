package org.jlore.io.msg
import org.jlore.io.ByteBuffer
import org.jlore.logging.Log
//import org.jlore.logging.ObjectLog

case class Fixed128MessageField (value:(Long,Long)) extends MessageField {
  import Fixed128MessageField._
  
  override def typeMarker = 6
  override def asDoubleLong:Option[(Long,Long)] = Some(value)
  override def encodeTo(buf: ByteBuffer) {
    writeLong(buf, value._1)
    writeLong(buf, value._2)
  }
}

object Fixed128MessageField extends Log {
  def apply (l1:Long, l2:Long) = new Fixed128MessageField((l1,l2))
  
  def read(buf: ByteBuffer) = {
    buf.ifRead(16) { bytes: Seq[Byte] =>
      log.debug("Reading: " + bytes.mkString(","))
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
    (bytes(offset + 0) & 0xFFl) |
    (bytes(offset + 1) & 0xFFl) << 8 |
    (bytes(offset + 2) & 0xFFl) << 16 |
    (bytes(offset + 3) & 0xFFl) << 24 |
    (bytes(offset + 4) & 0xFFl) << 32 |
    (bytes(offset + 5) & 0xFFl) << 40 |
    (bytes(offset + 6) & 0xFFl) << 48 |
    (bytes(offset + 7) & 0xFFl) << 56
}