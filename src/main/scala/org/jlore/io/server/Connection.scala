package org.jlore.io.server
import scala.actors.Actor
import java.nio.channels.SocketChannel
import org.jlore.logging.Log
import org.jlore.io.ByteBuffer
import org.jlore.io.msg.VarIntMessageField
import org.jlore.io.msg.Message

class Connection(server: Server, channel:SocketChannel) extends Actor with Log {
  import Connection._
  
  private val buf = new ByteBuffer()
  private var messageSize:Option[Int] = None
  private val protocol = new SystemProtocolFactory().instantiate()
  
  def act() {
    log.info("A connection acting!")
    loop {
      react { 
        case Read(bytes) =>
          buf << bytes
          parse()
        case Closed =>
          log.info("A connection exiting!")
          server.system ! System.Done
          exit()
      }
    }
  }
  
  private def parse() {
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
  
  private def process (message: Message) {
    protocol.read(message).foreach(server.system ! _)
  }
}

object Connection {
  sealed abstract class ActorMessage
  case class Read(bytes: Array[Byte]) extends ActorMessage
  case object Closed extends ActorMessage  
}