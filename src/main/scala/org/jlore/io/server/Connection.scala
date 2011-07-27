package org.jlore.io.server
import scala.actors.Actor

class Connection extends Actor {
  import Connection._
  def act() {
    println("A connection acting!")
    loop {
      react { 
        case Read(bytes) =>
          println (new String(bytes))
        case Closed =>
          println("A connection exiting!")
          exit()
      }
    }
  }
}

object Connection {
  sealed abstract class Message
  case class Read(bytes: Array[Byte]) extends Message
  case object Closed extends Message  
}