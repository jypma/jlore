package org.jlore.io
import org.jlore.core.ID

abstract class SerializerField[T,F,G](val writer: T=>F) {
  var readValue = null.asInstanceOf[F]
  def read (buf: ByteBuffer) = doRead(buf) match {
    case Some(f) => readValue = f.asInstanceOf[F]; Some(f)
    case None => None
  }
  def write (obj: T, buf: ByteBuffer) {
    doWrite(writer(obj).asInstanceOf[G], buf)
  }
  protected def doRead(buf: ByteBuffer): Option[G]
  protected def doWrite(value:G, buf:ByteBuffer)
}

object SerializerField {
  def apply[T,F: ClassManifest](writer: T=>F): SerializerField[T,F,_] = {
    classManifest[F] match {
      case m if m <:< ClassManifest.Int => field(writer, 4, readInt, writeInt)  
      case m if m <:< ClassManifest.Long => field(writer, 8, readLong, writeLong)
      case m if m <:< classManifest[String] => new SerializerField[T,F,String](writer) {
        override def doWrite(value:String, buf: ByteBuffer) {
          val bytes = value.getBytes("UTF-8")
          writeInt(bytes.length, buf)
          buf << bytes
        }
        override def doRead(buf: ByteBuffer):Option[String] = {
          buf.ifPeek(4)(readInt) flatMap { length =>
            buf.ifRead(4 + length) { bytes => 
              new String(bytes.slice(4, bytes.length).toArray, "UTF-8") 
            }
          }
        }      
      }
      case m if m <:< classManifest[ID] => new SerializerField[T,F,ID](writer) {
        override def doWrite(value:ID, buf: ByteBuffer) {
          writeLong(value.node, buf)
          writeLong(value.seq, buf)
          writeLong(value.time, buf)          
        }
        override def doRead(buf: ByteBuffer):Option[ID] = {
          buf.ifRead(24) { bytes =>
            ID.load(readLong(bytes.slice(0, 8)), 
                    readLong(bytes.slice(8, 16)), 
                    readLong(bytes.slice(16, 24)))
          }
        }      
      }
      case default => 
        throw new IllegalArgumentException ("Unsupported field " + classManifest[F])
    }
  }
  
  private def field[T,F,G](writer: T=>F, expected: Int, doReadImpl: Seq[Byte]=>G, doWriteImpl: (G,ByteBuffer)=>_) = {
    new SerializerField[T,F,G](writer) {
      override def doRead(buf: ByteBuffer):Option[G] = {
        buf.ifRead(expected) { bytes => doReadImpl(bytes) }
      }
      override def doWrite(value:G, buf: ByteBuffer) {
        doWriteImpl(value, buf)
      }
    }
  }
  
  val readInt = (b:Seq[Byte]) => b(0) << 24 | b(1) << 16 | b(2) << 8 | b(3) 
  val writeInt = (v:Int, buf:ByteBuffer) => buf << 
                  ((v >> 24) & 0xff).asInstanceOf[Byte] <<
                  ((v >> 16) & 0xff).asInstanceOf[Byte] <<  
                   ((v >> 8) & 0xff).asInstanceOf[Byte] <<  
                          (v & 0xff).asInstanceOf[Byte]  
  
  val readLong = (b:Seq[Byte]) => (b(0) << 56).asInstanceOf[Long] | 
                                           b(1) << 48 | b(2) << 40 | b(3) << 32 |
                                           b(4) << 24 | b(5) << 16 | b(6) << 8 | b(7)
  val writeLong = (v:Long, buf:ByteBuffer) => buf << 
                    ((v >> 56) & 0xff).asInstanceOf[Byte] <<
                    ((v >> 48) & 0xff).asInstanceOf[Byte] <<
                    ((v >> 40) & 0xff).asInstanceOf[Byte] <<
                    ((v >> 32) & 0xff).asInstanceOf[Byte] <<
                    ((v >> 24) & 0xff).asInstanceOf[Byte] <<
                    ((v >> 16) & 0xff).asInstanceOf[Byte] <<  
                     ((v >> 8) & 0xff).asInstanceOf[Byte] <<  
                            (v & 0xff).asInstanceOf[Byte]  
}