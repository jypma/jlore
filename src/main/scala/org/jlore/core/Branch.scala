package org.jlore.core
import scala.collection.immutable.TreeMap

class Branch (
  val parent: Branch = Branch.top,
  val command: Command = new Branch.Create(ID()),
  val tail: TreeMap[Command,Branch] = TreeMap.empty,
  val latestVersions: Map[VersionedObject[Any],ObjectVersion[Any]] = Map.empty
) extends Ordered[Branch] {
  def compare (other:Branch) = command.compare (other.command)
  def + (c: Command): Branch = {
    if (c > command) {
      val newObjs = c.run(this)
      val newVersions = latestVersions ++ (newObjs map { v => (v.obj -> v) })
      val newTail = tail + (command -> this)
      new Branch (parent, c, newTail, newVersions)
    } else {
      var branch = tail.to (c).last._2
      // create new branches for rest of tail
      tail.from(c).keys.foreach { cmd =>
        branch += cmd
      }
      // create new branch for head and return that
      branch + command
    }
  }
  def get [T] (obj: VersionedObject[T]): T = {
    latestVersions.getOrElse (obj, parent.get(obj)).asInstanceOf[T]
  }
}

object Branch {
  val top = new Branch(null) 
  
  class Create (id:ID) extends Command(id) {
    def run (b: Branch) = Nil
  }
}