package org.jlore.core

abstract class Command {
  def run (b: Branch): Seq[ObjectVersion]
}