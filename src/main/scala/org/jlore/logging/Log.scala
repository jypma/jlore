package org.jlore.logging

trait Log {
  protected val log = new Logger(getClass.getName)
}

class ObjectLog {
  private val log = new Logger(getClass.getName)
  def _log = log
}