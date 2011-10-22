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
        case SelectBranch(branchShortcut, branchId) =>
          log.debug("Shortcut " + branchShortcut + " to " + branchId)
        case PushCommands(branchShortcut, commandMsgs) =>
          log.debug("To " + branchShortcut + " pushing " + commandMsgs)
        case Done =>
          exit()
      }
    }
  }
}

object ConnectionWorker {
  sealed abstract class ActorMessage
  case class SelectBranch(branchShortcut: Int, branchId: ID) extends ActorMessage
  case class PushCommands(branchShortcut: Int, commandMsgs: Seq[Message]) extends ActorMessage
  case object Done extends ActorMessage  
}