package org.jlore.io

import scala.collection.immutable.TreeMap
import org.jlore.io.msg.Message
import org.jlore.logging.Log

class ProtocolFactory (initializers: AnyRef*) extends Log {
  private var readers: Map[Int, Map[Int, () => Serializer[_]]] = Map.empty
  private var writers: Map[Class[_], (Int, Int, () => Serializer[_ >: AnyRef])] = Map.empty
  
  def register[T : Manifest] (msg: Int, version:Int, protocol: =>Serializer[T]) {
    log.debug("" + this + ": Registering " + msg + "," + version + " as " + protocol)
    val f = {() => protocol}
    val c = manifest[T].erasure.asInstanceOf[Class[T]]
    log.info("Installing for " + c)
    if (readers.get(msg).flatMap(_.get(version)).isDefined) {
      throw new IllegalArgumentException ("Duplicate definition of msg " + msg
          + ", version " + version + " in " + this)
    }
    
    readers += (msg -> (readers.getOrElse(msg, Map.empty) + (version -> f)))
    val existingWriter = writers.get(c)
    if (existingWriter.isEmpty || version > existingWriter.get._2) {
      val entry = (c.asInstanceOf[Class[_]] -> (msg, version, f.asInstanceOf[() => Serializer[_ >: AnyRef]]))
      writers = writers + entry  
    }
    log.debug("Writers are now " + writers)
  }
  
  def instantiate() = new Protocol (readers.mapValues { _.mapValues { _() } },
                                    writers.mapValues { m => (m._1, m._2, m._3()) })
}
