package org.jlore.io.msg

import org.jlore.io.ByteBuffer

trait MessageField {
  def typeMarker:Int
  
  def asInt:Option[Int] = None
  def asLong:Option[Long] = None
  def asBytes:Seq[Byte] = Nil
  
  def asString:Option[String] = {
    val bytes = asBytes
    if (bytes.isEmpty) None else Some(new String(bytes.toArray,"UTF-8"))
  }
  
  def asMessage:Option[Message] = Message(ByteBuffer(asBytes))
  
  def encodeTo(buf: ByteBuffer)
}

object MessageField {
  def apply(i:Int) = VarIntMessageField(i)
  def apply(l:Long) = VarIntMessageField(l)
  def apply(m:Message) = LengthDelimitedMessageField(m.encode)
}