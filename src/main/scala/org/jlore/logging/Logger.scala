package org.jlore.logging

class Logger(val name: String) {
  import LogLevel._
  import Appender.out
  var limit = TRACE
  def trace (msg: => AnyRef) { if (isLevel(TRACE)) out (name, TRACE, msg) }
  def debug (msg: => AnyRef) { if (isLevel(DEBUG)) out (name, DEBUG, msg) }
  def info (msg: => AnyRef) { if (isLevel(INFO)) out (name, INFO, msg) }
  def warn (msg: => AnyRef) { if (isLevel(WARN)) out (name, WARN, msg) }
  def error (msg: => AnyRef) { if (isLevel(ERROR)) out (name, ERROR, msg) }
  def fatal (msg: => AnyRef) { if (isLevel(FATAL)) out (name, FATAL, msg) }
  private def isLevel(level: LogLevel) = limit <= level
}
 