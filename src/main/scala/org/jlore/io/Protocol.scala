package org.jlore.io
import scala.collection.immutable.TreeMap

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
  private var currentReader: Option[Serializer[_]] = None
  private var currentMsg: Option[Int] = None
  private var currentVersion: Option[Int] = None
  
  def read[T](buf: ByteBuffer, done: T=>Unit) {
    if (currentMsg.isEmpty) {
      currentMsg = buf.read(4)(SerializerField.readInt)
      if (currentMsg.isEmpty) return
    }
    if (currentVersion.isEmpty) {
      currentVersion = buf.read(4)(SerializerField.readInt)
      if (currentVersion.isEmpty) return
    }
    
  }
}