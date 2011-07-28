package org.jlore.logging

sealed abstract class LogLevel (val level: Int, val name: String) extends Ordered[LogLevel] {
  def compare (other:LogLevel) = level - other.level
  override def toString = name
}
object LogLevel {
  case object FATAL extends LogLevel(5, "FATAL")
  case object ERROR extends LogLevel(4, "ERROR")
  case object WARN extends LogLevel(3, "WARN")
  case object INFO extends LogLevel(2, "INFO")
  case object DEBUG extends LogLevel(1, "DEBUG")
  case object TRACE extends LogLevel(0, "TRACE")
}
