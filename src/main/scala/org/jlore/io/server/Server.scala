package org.jlore.io.server

import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SelectionKey
import scala.collection.JavaConversions._
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.nio.channels.spi.SelectorProvider
import java.net.InetSocketAddress
import java.net.InetAddress
import java.io.IOException
import java.util.concurrent.LinkedBlockingQueue
import scala.collection.mutable.ArrayBuffer
import org.jlore.logging.Log

class Server (val host: InetAddress, val port: Int, val system:System) extends Log {
  def this(port: Int, system:System) = this(null, port, system)
  private var thread: Option[ServerThread] = None
  
  class ServerThread extends Thread {
    var running = true
    val readBuffer = ByteBuffer.allocate(8192)
    val selector = SelectorProvider.provider.openSelector
    val serverChannel = ServerSocketChannel.open
    serverChannel.configureBlocking(false)
    serverChannel.socket.bind (new InetSocketAddress(host, port))
    serverChannel.register (selector, SelectionKey.OP_ACCEPT)
    private val changes = new LinkedBlockingQueue[() => Unit]()
    private var pendingData: Map[SocketChannel,ArrayBuffer[ByteBuffer]] = Map.empty
    
    override def run() {
      while (true) {
        handleChanges()
        selector.select
        if (isInterrupted) {
          for (key <- selector.keys; if key.attachment.isInstanceOf[Connection])
            key.attachment.asInstanceOf[Connection] ! Connection.Closed
          serverChannel.close
          return
        }
        for (key <- selector.selectedKeys; if key.isValid){
          if (key.isAcceptable) accept(key) 
          else if (key.isReadable) read(key)
          else if (key.isWritable) write(key)
        }
        selector.selectedKeys.clear
      }
    }
    
    private def handleChanges() {
      while (!changes.isEmpty) {
        val change = changes.remove()
        log.info("handling " + change)
        change()
      }
    }
    
    private def accept(key: SelectionKey) {
      val socketChannel = key.channel.asInstanceOf[ServerSocketChannel].accept
      socketChannel.configureBlocking(false)
      socketChannel.register (selector, SelectionKey.OP_READ).attach(
          new Connection(Server.this, socketChannel).start)
    }
    
    private def read(key: SelectionKey) {
      val socketChannel = key.channel.asInstanceOf[SocketChannel]
      readBuffer.clear
      try {
        if (socketChannel.read(readBuffer) == -1) 
          close(key)
        else 
          key.attachment.asInstanceOf[Connection] ! Connection.Read(readBuffer.array)
        
      } catch {
        case x: IOException => 
          close(key)
      }
    }
    
    private def close(key: SelectionKey) {
      key.channel.close
      key.cancel
      key.attachment.asInstanceOf[Connection] ! Connection.Closed
    }
    
    private def write(key: SelectionKey) {
      log.info("writing")
      val socketChannel = key.channel.asInstanceOf[SocketChannel]
      synchronized {
        val list = pendingData(socketChannel)
        log.info("found " + list.size + " items for " + socketChannel)
        var socketFull = false
        while (!list.isEmpty || socketFull) {
          val buf = list(0)
          log.info("writing " + buf)
          socketChannel.write(buf)
          socketFull = buf.remaining() > 0
          if (!socketFull) list.remove(0)
        }
        if (list.isEmpty) {
           log.info("interest back in read")
           key.interestOps(SelectionKey.OP_READ);
        }
      }
    }
    
    def send(socket: SocketChannel, data: Array[Byte]) {
      log.info("putting changes")
      changes.put { () =>
        log.info("interest in write")
        val key = socket.keyFor(selector)
        key.interestOps(SelectionKey.OP_WRITE);
      }
      log.info("pending data")
      synchronized {
        var newList = pendingData.getOrElse(socket, ArrayBuffer.empty)
        newList += ByteBuffer.wrap(data)
        pendingData += (socket -> newList)
      }
      log.info("wakeup selector")
      selector.wakeup();
    }
  }
  
  def start() {
    synchronized {
      if (thread.isEmpty) {
        thread = Some(new ServerThread())
        thread.get.start
      }
    }
  }
  
  def send(socket: SocketChannel, data: Array[Byte]) {
    if (thread.isDefined) thread.get.send(socket, data)    
  }
  
  def stop() {
    synchronized {
      if (thread.isDefined) {
        thread.get.running = false
        thread.get.interrupt
        thread.get.join
        thread = None
      }
    }
  }
}