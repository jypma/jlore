package org.jlore.io.server
import scala.actors.Actor
import java.nio.channels.SocketChannel
import org.jlore.logging.Log

class Connection(server: Server, channel:SocketChannel) extends Actor with Log {
  import Connection._
  def act() {
    log.info("A connection acting!")
    loop {
      react { 
        case Read(bytes) =>
          log.info(new String(bytes))
          server.send(channel, bytes)
        case Closed =>
          log.info("A connection exiting!")
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