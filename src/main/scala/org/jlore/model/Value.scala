package org.jlore.model

abstract class Value  { 
  def asString: String
  def asInt: Option[Int]
}

case class StringValue (val s: String) extends Value {
  def asString = s
  def asInt = try {
    Some(s.toInt)
  } catch {
    case _ : java.lang.NumberFormatException => None
  }
}

case class IntValue (val i: Int) extends Value {
  def asString = i.toString
  def asInt = Some(i)
}

object Value {
  def apply(s:String) = new StringValue(s)
  def apply(i:Int) = new IntValue(i)
  val _0 = apply(0)
  val _1 = apply(1)
}