package org.jlore.io.msg

class MessageFields(val values:Seq[MessageField]) {
  def asInt:Option[Int] = values.head.asInt
  def asInts:Seq[Int] = values map (_.asInt.get)
  
  def +(b: MessageField) = new MessageFields(b +: values) 
}