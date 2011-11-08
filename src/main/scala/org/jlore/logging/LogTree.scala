package org.jlore.logging
import scala.actors.Actor

class LogTree extends Actor {
  import LogTree._
  private var root: LogNode = new LogNode("")

  def act() {
    loop {
      react { 
        case GetNode(path) =>
          reply(root.subNode(path.split("\\.")))
        case GetClassNode(cls) =>
          reply(root.subNode(nameOf(cls).split("\\.")))
        case Done =>
          exit()
      }
    }
  }
  
  private def nameOf(cls:Class[_]) = if (cls.getName().endsWith("$")) {
    cls.getName().substring(0, cls.getName().length() - 1) + ".$"
  } else cls.getName()
}

object LogTree {
  sealed abstract class ActorMessage
  case class GetNode(path: String) extends ActorMessage
  case class GetClassNode(cls: Class[_]) extends ActorMessage
  case object Done extends ActorMessage
  
  private val tree = new LogTree()
  tree.start()
  
  private var cache: Map[Class[_], LogNode] = Map.empty
  
  implicit def getNode(path: String) = (tree !? GetNode(path)).asInstanceOf[LogNode]
  implicit def getClassNode(cls: Class[_]) = cache.getOrElse(cls, {
    val node = (tree !? GetClassNode(cls)).asInstanceOf[LogNode]
    cache += (cls -> node)
    node
  })
  
  private val config = Config
}