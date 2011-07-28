package org.jlore.logging

trait Log {
  protected val log = new Logger(getClass.getName)
}