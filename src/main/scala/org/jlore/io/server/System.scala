package org.jlore.io.server
import scala.actors.Actor
import org.jlore.logging.Log
import org.jlore.core.ID
import org.jlore.io.msg.Message
import org.jlore.core.Command

class System extends Actor with Log {
  import System._
  
  var branches = Map.empty[ID,BranchManager]
  
  def act() {
    loop {
      react {
        case Push(branchId, commandMsgs) =>
          log.debug("To " + branchId + " pushing " + commandMsgs)
        case Done =>
          branches.values.foreach (_ ! BranchManager.Done)
          exit()
      }
    }
  }
}

object System {
  sealed abstract class ActorMessage
  case class Push(branchId: Option[ID], commands: Seq[Command]) extends ActorMessage
  case object Done extends ActorMessage  
}