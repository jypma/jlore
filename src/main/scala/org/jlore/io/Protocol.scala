package org.jlore.io
import scala.collection.immutable.TreeMap
import org.jlore.io.msg.Message
import org.jlore.logging.Log

class Protocol(private val readers: Map[Int,Map[Int,Serializer[_]]],
               private val writers: Map[Class[_], (Int,Int,Serializer[_ >: AnyRef])]) extends Log {
  def read[T:Manifest](msg: Message) = {
    val result = readers.get(msg.msg) flatMap (_.get(msg.version)) map (_.read(msg))
    result flatMap { obj:Any =>
      if (obj.isInstanceOf[T]) Some(obj.asInstanceOf[T]) else None 
    }
  }
  
  def read[T:Manifest](msgs:Seq[Message]): Seq[T] = msgs map (msg => read(msg).get)

  def write[T >: AnyRef](obj:T) = {
    log.debug("Writer for " + obj.getClass + " is " + writers.get(obj.getClass))
    writers.get(obj.getClass) map (w => w._3.write(obj.asInstanceOf[AnyRef], 
        new Message(w._1, w._2)))
  }
  
  def write[T >: AnyRef](objs:Seq[T]): Seq[Message] = objs map (obj => write(obj).get)
}