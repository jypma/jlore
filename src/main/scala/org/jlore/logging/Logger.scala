package org.jlore.logging
import LogLevel._
import Appender.out

class Logger(val node: LogNode) {
  import node.name
  def trace (msg: => AnyRef) { if (isLevel(TRACE)) out (name, TRACE, msg) }
  def debug (msg: => AnyRef) { if (isLevel(DEBUG)) out (name, DEBUG, msg) }
  def info (msg: => AnyRef) { if (isLevel(INFO)) out (name, INFO, msg) }
  def warn (msg: => AnyRef) { if (isLevel(WARN)) out (name, WARN, msg) }
  def error (msg: => AnyRef) { if (isLevel(ERROR)) out (name, ERROR, msg) }
  def fatal (msg: => AnyRef) { if (isLevel(FATAL)) out (name, FATAL, msg) }
  private def isLevel(level: LogLevel) = node.level <= level
}
 