package org.jlore.core
import scala.collection.immutable.TreeMap
import scala.collection.immutable.TreeSet

class Branch (
  val parent: Branch = Branch.top,
  val command: Command = new Branch.Create(ID()),
  val tail: TreeMap[Command,Branch] = TreeMap.empty,
  val ancestors: List[Branch] = Nil,
  val latestVersions: Map[VersionedObject[Any],ObjectVersion[Any]] = Map.empty
) extends Ordered[Branch] {
  def compare (other:Branch) = command.compare (other.command)
  def prev = ancestors.headOption.getOrElse(parent)
  def + (c: Command): Branch = {
    if (c > command) {
      c.run(this)
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
  
  case class Create (id:ID) extends Command {
    def run (b: Branch) = b
  }
  case class Merge (id:ID, sub: Branch) extends Command {
    def run (b: Branch) = {
      println("finding ancestor of sub " + sub + " and b " + b)
      // Find earliest ancestor of [sub] that is also in [b]
      var (ancestor, tail) = findAncestorIn (sub, b)
      
      // Weave (sort), de-duplicate, and run all commands from both branches from there
      // TODO replace with ++ operator (which assumes commands are ordered)
      tail.foreach { c:Command =>
        println("  adding " + c)
        ancestor += c
      }
      ancestor
    }
  }
  
  private def findAncestorIn (a:Branch, b: Branch, commands:List[Command] = Nil): (Branch, List[Command]) = {
    println("find ancestor in " + a + ", " + b)
    if (a == b) (a, commands) else
    	if (a < b) findAncestorIn (a, b.prev, b.command :: commands) else
                   findAncestorIn (a.prev, b, a.command :: commands)
  }
  
}