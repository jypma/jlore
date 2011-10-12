package org.jlore.io
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.WrappedArray
import scala.collection.mutable

class ByteBuffer extends ByteWriter[ByteBuffer] {
  private var buffers: ArrayBuffer[mutable.Seq[Byte]] = ArrayBuffer.empty
  private var pos = 0
  
  def << (buf: Array[Byte]) = append(buf)
  
  def << (byte: Byte) = append(byte)
  
  def append(buf: Array[Byte]) = { buffers += buf; this }
  
  def append(byte: Byte) = { 
    if (buffers.isEmpty || !buffers.last.isInstanceOf[ArrayBuffer[Byte]]) {
      buffers += new ArrayBuffer[Byte]()
    }
    buffers.last.asInstanceOf[ArrayBuffer[Byte]] += byte
    this
  }
  
  def read[T](count:Int)(process: Seq[Byte]=>T): Option[T] = {
    if (!available(count)) None else Some(process(read(count)))
  }
  
  def peek[T](count:Int)(process: Seq[Byte]=>T): Option[T] = {
    if (!available(count)) None else Some(process(peek(count)))    
  }
  
  private def read(count:Int):Seq[Byte] = {
    if (pos + count < buffers.head.length) {
      val result = buffers.head.slice(pos, pos + count)
      pos += count
      result
    } else {
      var needed = count
      var result = new ArrayBuffer[Byte](count)
      while (needed > 0) {
        if (pos < buffers.head.length) {
        	result ++= buffers.head.slice(pos, buffers.head.length)
        }
        needed -= (buffers.head.length - pos)
        pos = 0
        buffers.remove(0)
      }
      result
    }
  }
  
  private def peek(count:Int):Seq[Byte] = {
    if (pos + count < buffers.head.length) {
      buffers.head.slice(pos, pos + count)
    } else {
      var needed = count
      var result = new ArrayBuffer[Byte](count)
      var ourpos = pos
      while (needed > 0) {
        if (ourpos < buffers.head.length) {
        	result ++= buffers.head.slice(ourpos, buffers.head.length)
        }
        needed -= (buffers.head.length - ourpos)
        ourpos = 0
        buffers.remove(0)
      }
      result
    }
  }
  
  //TODO keep track of available bytes in a field instead
  def available(count:Int) = {
    var needed = count + pos
    buffers.find { buf =>
      needed -= buf.length
      needed <= 0
    }.isDefined
  }
}