package org.jlore.io.msg
import org.jlore.io.ByteBuffer

class Message (val msg:Int, val version:Int, val fields:Map[Int,MessageFields]) {
  def write(buf: ByteBuffer) {
    
  }
  def apply(index: Int) = fields(index)
}

object Message {
  def apply (bytes: ByteBuffer): Option[Message] = {
    VarIntMessageField.read(bytes) flatMap (_.asInt flatMap { msg =>
      VarIntMessageField.read(bytes) flatMap (_.asInt flatMap { version =>
        var fields = Map.empty[Int, MessageFields]
        while (bytes.available(1)) {
          val key = VarIntMessageField.read(bytes) flatMap (_.asInt)
          if (key.isEmpty) return None
          val index = (key.get >> 3)
          val field = (key.get & 0x07) match {
            case 0 => VarIntMessageField.read(bytes)
            case 2 => LengthDelimitedMessageField.read(bytes)
            case default => None
          }
          if (field.isEmpty) return None
          fields += (index -> (fields.get(index) map (_ + field.get) getOrElse 
              new MessageFields(field.get +: Nil)))
        }
        Some(new Message(msg,version,fields))
      })
    })
  }
   
}