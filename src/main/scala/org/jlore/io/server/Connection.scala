package org.jlore.io.server
import scala.actors.Actor
import java.nio.channels.SocketChannel
import org.jlore.logging.Log
import org.jlore.io.ByteBuffer
import org.jlore.io.msg.VarIntMessageField
import org.jlore.io.msg.Message

class Connection(server: Server, channel:SocketChannel) extends Actor with Log {
  import Connection._
  
  val worker = new ConnectionWorker()
  val buf = new ByteBuffer()
  var messageSize:Option[Int] = None
  
  def act() {
    log.info("A connection acting!")
    loop {
      react { 
        case Read(bytes) =>
          buf << bytes
          parse()
        case Closed =>
          log.info("A connection exiting!")
          worker ! ConnectionWorker.Done
          exit()
      }
    }
  }
  
  def parse() {
    if (!messageSize.isDefined) {
      messageSize = VarIntMessageField.read(buf) map { _.asInt.get }
    }
    if (messageSize.isDefined) {
      buf.ifRead(messageSize.get) { buf:Seq[Byte] =>
        messageSize = None        
        Message (ByteBuffer(buf))
      }.foreach { _.foreach { process(_) } }
    }
  }
  
  def process (message: Message) {
    /*
    message.msg match {
      case 0 => worker ! ConnectionWorker.SelectBranch(message(0).toInt.get, message(1).toID.get)
      case 1 => worker ! ConnectionWorker.PushCommands(message(0).toInt.get, message(1).toMessages.get)
    }
    */
    // unpack using ConnectionProtocolFactory
    // messages therein: 
    //   SelectBranch -> Register a shortcut for refering to a branch by int in later msgs
    //                   (0 is implied to be MASTER branch)
    //   PushCommands -> Upload a bunch of commands to a given branch
  }
}

object Connection {
  sealed abstract class ActorMessage
  case class Read(bytes: Array[Byte]) extends ActorMessage
  case object Closed extends ActorMessage  
}