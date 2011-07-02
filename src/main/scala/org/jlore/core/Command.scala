package org.jlore.core

abstract class Command (val id: ID) extends Ordered[Command] {
  def compare (other:Command) = id.compare (other.id)
  def run (b: Branch): Seq[ObjectVersion[Any]]
}