package org.jlore.io.msg

import org.jlore.io.ByteBuffer

class LengthDelimitedMessageField (val bytes:Seq[Byte]) extends MessageField {
  override def typeMarker = 2
  override def asBytes:Seq[Byte] = bytes
  override def encodeTo(buf: ByteBuffer) {
    new IntVarIntMessageField(bytes.length).encodeTo(buf)
    buf << bytes
  }
}

object LengthDelimitedMessageField {
  def read(buf: ByteBuffer) = {
    VarIntMessageField.read(buf) flatMap (_.asInt) flatMap 
      (buf.read(_)) map (new LengthDelimitedMessageField(_))
  }
}