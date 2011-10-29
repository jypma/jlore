package org.jlore.logging

trait Log {
  protected val log = new Logger(getClass.getName)
}

class ObjectLog {
  private val _log = new Logger(getClass.getName)
  def getlog = _log
}