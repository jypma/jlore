package org.jlore.logging

trait Log {
  protected val log = new Logger(LogTree.getClassNode(getClass))
}

object Log {
  def log(obj:AnyRef) = new Logger(LogTree.getClassNode(obj.getClass))
}