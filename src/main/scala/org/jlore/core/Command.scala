package org.jlore.core

abstract class Command extends Ordered[Command] {
  def id: ID
  def compare (other:Command) = id.compare (other.id)
  def run (b: Branch): Branch
}

abstract class ChangeCommand extends Command {
  def run (b: Branch) = {
    val newObjs = change(b)
    val newVersions = b.latestVersions ++ (newObjs map { v => (v.obj -> v) })
    val newTail = b.tail + (b.command -> b)
    val newAncestors = b :: b.ancestors
    new Branch (b.parent, this, newTail, newAncestors, newVersions)
  }
  
  def change (b: Branch): Iterable[ObjectVersion[Any]]
}