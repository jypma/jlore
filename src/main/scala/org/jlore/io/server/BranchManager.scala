package org.jlore.io.server
import scala.actors.Actor
import org.jlore.logging.Log
import org.jlore.core.Command

class BranchManager extends Actor with Log {
  import BranchManager._
  
  def act() {
    loop {
      react {
        case Push(commandMsgs) =>
          log.debug("To " + this + " pushing " + commandMsgs)
        case Done =>
          exit()
      }
    }
  }

}

object BranchManager {
  sealed abstract class ActorMessage
  case class Push(commands: Seq[Command]) extends ActorMessage
  case object Done extends ActorMessage  
}