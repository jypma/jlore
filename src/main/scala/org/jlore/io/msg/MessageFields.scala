package org.jlore.io.msg

class MessageFields(val values:Seq[MessageField] = Nil) {
  def asInt = values.headOption flatMap (_.asInt)
  def asLong = values.headOption flatMap (_.asLong)
  def asDoubleLong = values.headOption flatMap (_.asDoubleLong)
  def asMessage = values.headOption flatMap (_.asMessage)
  def asString = values.headOption flatMap (_.asString)

  def asInts = values map (_.asInt) filter (_.isDefined) map (_.get)
  def asLongs = values map (_.asLong) filter (_.isDefined) map (_.get)
  def asMessages = values map (_.asMessage) filter (_.isDefined) map (_.get)
  
  def +(b: MessageField) = new MessageFields(b +: values)
  
  def isEmpty = values.isEmpty
}