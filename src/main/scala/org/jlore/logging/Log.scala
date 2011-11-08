package org.jlore.logging

trait Log {
  protected val log = new Logger(LogTree.getClassNode(getClass))
}

class ObjectLog {
  private val log = new Logger(LogTree.getClassNode(getClass))
  def _log = log
}