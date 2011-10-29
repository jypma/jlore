package org.jlore.io
import org.jlore.core.ID
import org.jlore.io.msg.Message
import org.jlore.io.msg.VarIntMessageField

abstract class SerializerField[T,F] (val index: Int, val writer: T=>F) {
  def write(obj:T, msg:Message): Message
}
/*
object SerializerField {
  def varint[T](index:Int, writer: T=>Int) = new SerializerField(index, writer) {
    def write(obj:T, msg:Message) = msg + (index -> VarIntMessageField(writer(obj)))
  }
*/