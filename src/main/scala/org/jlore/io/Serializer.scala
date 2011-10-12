package org.jlore.io

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap

trait Serializer[T] {
  private val fields = ArrayBuffer.empty[SerializerField[T,_,_]]
  private var currentFieldIdx: Int = 0
  
  protected def field[F : Manifest](writer: T=>F) = {
    val f = SerializerField(writer)
    fields += f
    f
  }
  
  def write(obj: T, buf: ByteBuffer) { fields.foreach(_.write(obj, buf)) }

  protected def load: T
  
  implicit def field2value[F](field: SerializerField[T,F,_]) = field.readValue
  
  def read(buf: ByteBuffer, done: T=>Unit) {
    while (fields(currentFieldIdx).read(buf).isDefined) {
      currentFieldIdx += 1
      if (currentFieldIdx >= fields.length) {
        done(load)
        currentFieldIdx = 0
      }
    }
  }
}