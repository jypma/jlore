package org.jlore.io.msg

import org.jlore.io.ByteBuffer

trait MessageField {
  def asInt:Option[Int] = None
  def asLong:Option[Long] = None
  def asString:Option[String] = {
    val bytes = asBytes
    if (bytes.isEmpty) None else Some(new String(bytes.toArray,"UTF-8"))
  }
  def asBytes:Seq[Byte] = Nil
  
  def encodeTo(buf: ByteBuffer)
}