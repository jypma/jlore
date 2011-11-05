package org.jlore.io.msg
import org.jlore.io.ByteBuffer
import org.jlore.logging.Log

class Message (val msg:Int, val version:Int, val fields:Map[Int,MessageFields] = Map.empty) {
  import Message.emptyField
  
  def encodeTo(buf: ByteBuffer) {
    VarIntMessageField(msg).encodeTo(buf)
    VarIntMessageField(version).encodeTo(buf)
    for ((key, fieldValues) <- fields; field <- fieldValues.values) {
      VarIntMessageField(key << 3 | field.typeMarker).encodeTo(buf)
      field.encodeTo(buf)
    }
  }
  def +(keyField:(Int,MessageField)) = {
    new Message(msg, version, fields + (keyField._1 -> 
        (fields.get(keyField._1) map (_ + keyField._2) getOrElse 
              new MessageFields(keyField._2 +: Nil))))
  }
  def apply(index: Int) = fields.getOrElse(index, emptyField) 
  def encode = {
    val buf = new ByteBuffer()
    encodeTo(buf)
    buf.asSeq
  }
}

object Message extends Log {
  private val emptyField = new MessageFields
  
  def apply (bytes: ByteBuffer): Option[Message] = {
    VarIntMessageField.read(bytes) flatMap (_.asInt flatMap { msg =>
      log.debug ("msg=" + msg)
      VarIntMessageField.read(bytes) flatMap (_.asInt flatMap { version =>
        log.debug ("version=" + version)
        var fields = Map.empty[Int, MessageFields]
        while (bytes.available(1)) {
          val key = VarIntMessageField.read(bytes) flatMap (_.asInt)
          log.debug ("key=" + key)
          if (key.isEmpty) return None
          val index = (key.get >> 3)
          log.debug ("index=" + index)
          log.debug ("type=" + (key.get & 0x07))
          val field = (key.get & 0x07) match {
            case 0 => VarIntMessageField.read(bytes)
            case 1 => Fixed64MessageField.read(bytes)
            case 2 => LengthDelimitedMessageField.read(bytes)
            case 6 => Fixed128MessageField.read(bytes)
            case default => None
          }
          log.debug("field=" + field)
          if (field.isEmpty) return None
          fields += (index -> (fields.get(index) map (_ + field.get) getOrElse 
              new MessageFields(field.get +: Nil)))
          log.debug("fields=" + fields)
        }
        Some(new Message(msg,version,fields))
      })
    })
  }
}