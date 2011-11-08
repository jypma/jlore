package org.jlore.io.server
import org.jlore.logging.Log
import scala.actors.Actor
import org.jlore.core.ID
import org.jlore.io.msg.Message

class ConnectionWorker extends Actor with Log {
  import ConnectionWorker._
  def act() {
    loop {
      react {
        case Push(branchId, commandMsgs) =>
          log.debug("To " + branchId + " pushing " + commandMsgs)
        case Done =>
          exit()
      }
    }
  }
}

object ConnectionWorker {
  sealed abstract class ActorMessage
  case class Push(branchId: Option[ID], commandMsgs: Seq[Message]) extends ActorMessage
  case object Done extends ActorMessage  
}