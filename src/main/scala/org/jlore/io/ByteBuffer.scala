package org.jlore.io
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.WrappedArray
import scala.collection.mutable
import org.jlore.logging.Log

class ByteBuffer extends ByteWriter[ByteBuffer] with Log {
  private var buffers: ArrayBuffer[Seq[Byte]] = ArrayBuffer.empty
  
  def << (buf: Seq[Byte]) = append(buf)  
  def << (byte: Byte) = append(byte)
  def << (byte: Int) = append(byte.toByte)
  def << (byte: Long) = append(byte.toByte)
  
  def append(buf: Seq[Byte]) = { buffers += buf; this }
  def append(byte: Int):ByteBuffer = append(byte.toByte)
  def append(byte: Long):ByteBuffer = append(byte.toByte)
  def append(byte: Byte) = { 
    if (buffers.isEmpty || !buffers.last.isInstanceOf[ArrayBuffer[Byte]]) {
      buffers += new ArrayBuffer[Byte]()
    }
    buffers.last.asInstanceOf[ArrayBuffer[Byte]] += byte
    this
  }
  
  def read[T](count:Int): Option[Seq[Byte]] = {
    if (!available(count)) None else Some(read(count))
  }
  
  def ifRead[T](count:Int)(process: Seq[Byte]=>T): Option[T] = {
    if (!available(count)) None else Some(process(read(count)))
  }
  
  def ifPeek[T](count:Int)(process: Seq[Byte]=>T): Option[T] = {
    if (!available(count)) None else Some(process(peek(count)))    
  }
  
  def readUntil(endCondition: Byte => Boolean): Option[Seq[Byte]] = {
    if (!available(1)) None else {
      var count = 0
      buffers.find {  
        _.find { b =>
          count += 1
          log.debug ("Checking " + b + " in " + this + ": " + endCondition(b))
          endCondition(b)
        }.isDefined
      }
      Some(read(count))
    }
  }
  
  def asSeq = buffers.fold(Seq.empty[Byte])(_ ++ _)
  
  private def read(count:Int):Seq[Byte] = {
    if (count < buffers.head.length) {
      val result = buffers.head.slice(0, count)
      buffers(0) = buffers.head.drop(count)
      result
    } else {
      var needed = count
      var result = new ArrayBuffer[Byte](count)
      while (needed > 0) {
        if (!buffers.head.isEmpty) {
        	result ++= buffers.head
        }
        needed -= buffers.head.length
        buffers.remove(0)
      }
      result
    }
  }
  
  private def peek(count:Int):Seq[Byte] = {
    if (count < buffers.head.length) {
      buffers.head.slice(0, count)
    } else {
      var needed = count
      var result = new ArrayBuffer[Byte](count)
      var ourpos = 0
      var ourbuffer = 0
      while (needed > 0) {
        if (ourpos < buffers(ourbuffer).length) {
        	result ++= buffers(ourbuffer).slice(ourpos, buffers(ourbuffer).length)
        }
        needed -= (buffers(ourbuffer).length - ourpos)
        ourpos = 0
        ourbuffer += 1
      }
      result
    }
  }
  
  //TODO keep track of available bytes in a field instead
  def size = {
    buffers.foldLeft(0)(_ + _.size)
  }
  
  def available(count:Int) = {
    var needed = count
    buffers.find { buf =>
      needed -= buf.length
      needed <= 0
    }.isDefined
  }
  
  override def toString = buffers.mkString 
}

object ByteBuffer {
  def apply(contents: Seq[Byte]) = {
    val buf = new ByteBuffer()
    buf << contents
    buf
  }
}