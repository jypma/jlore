package org.jlore.core
import scala.collection.immutable.TreeSet

class Branch (
  val command: Command = new Branch.Create(ID()),
  val tail: TreeSet[Branch] = TreeSet.empty,
  val latestVersions: Map[VersionedObject[Any],ObjectVersion[Any]] = Map.empty
) extends Ordered[Branch] {
  def compare (other:Branch) = command.compare (other.command)
  def + (c: Command) = {
    val newObjs = c.run(this)
    val newVersions = latestVersions ++ (newObjs map { v => (v.obj -> v) })
    new Branch (c, tail + this, newVersions)
  }
  def get [T] (obj: VersionedObject[Any]): T = {
    latestVersions(obj).asInstanceOf[T]
  }
}

object Branch {
  class Create (id:ID) extends Command(id) {
    def run (b: Branch) = Nil
  }
}