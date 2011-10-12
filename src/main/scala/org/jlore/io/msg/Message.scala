package org.jlore.io.msg
import org.jlore.io.ByteBuffer

class Message (val msg:Int, val version:Int, val fields:Map[Int,MessageField]) {
  def write(buf: ByteBuffer) {
    
  }
}