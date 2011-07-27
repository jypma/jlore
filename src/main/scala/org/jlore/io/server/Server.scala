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

class Server (host: InetAddress, port: Int) {
  def this(port: Int) = this(null, port)
  private var thread: Option[ServerThread] = None
  
  class ServerThread extends Thread {
    var running = true
    val readBuffer = ByteBuffer.allocate(8192)
    val selector = SelectorProvider.provider.openSelector
    val serverChannel = ServerSocketChannel.open
    serverChannel.configureBlocking(false)
    serverChannel.socket.bind (new InetSocketAddress(host, port))
    serverChannel.register (selector, SelectionKey.OP_ACCEPT)
    
    override def run() {
      while (true) {
        selector.select
        for (key <- selector.selectedKeys; if key.isValid){
          if (key.isAcceptable) accept(key) 
          else if (key.isReadable) read(key)
        }
        selector.selectedKeys.clear
      }
    }
    
    private def accept(key: SelectionKey) {
      val socketChannel = key.channel.asInstanceOf[ServerSocketChannel].accept
      socketChannel.configureBlocking(false)
      socketChannel.register (selector, SelectionKey.OP_READ).attach(new Connection().start)
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
  }
  
  def start() {
    synchronized {
      if (thread.isEmpty) {
        thread = Some(new ServerThread())
        thread.get.start
      }
    }
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