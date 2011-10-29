package org.jlore.io
import scala.collection.immutable.TreeMap
import org.jlore.io.msg.Message

class ProtocolFactory {
  private var readers: Map[Int, Map[Int, () => Serializer[_]]] = Map.empty
  private var writers: Map[Class[_], Map[Int, () => Serializer[_]]] = Map.empty
  
  def register[T : Manifest] (msg: Int, version:Int, protocol: =>Serializer[T]) {
    val f = {() => protocol}
    val c = manifest[T].erasure.asInstanceOf[Class[T]]
    readers += (msg -> (readers.getOrElse(msg, Map.empty) + (version -> f)))
    writers += (c -> (writers.getOrElse(c, Map.empty) + (version -> f)))
  }
  
  def instantiate() = new Protocol (readers.mapValues { _.mapValues { _() } },
                                    writers.mapValues { m => m(m.keys.max)() })
}

class Protocol(private val readers: Map[Int,Map[Int,Serializer[_]]],
               private val writers: Map[Class[_], Serializer[_]]) {
  def read[T:Manifest](msg: Message) = {
    val result = readers.get(msg.msg) flatMap (_.get(msg.version)) map (_.read(msg))
    result flatMap { obj:Any =>
      if (obj.isInstanceOf[T]) Some(obj) else None 
    }
  }

}