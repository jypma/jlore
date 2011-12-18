package org.jlore.io

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap
import org.jlore.core.ID
import org.jlore.io.msg.Message
import org.jlore.io.msg.MessageField
import org.jlore.io.msg.VarIntMessageField
import msg.Fixed128MessageField
import org.jlore.core.VersionedObject
import org.jlore.io.msg.MessageFields
import org.jlore.model.Value
import org.jlore.model.StringValue
import org.jlore.model.IntValue
import org.jlore.io.msg.LengthDelimitedMessageField

trait Serializer[T] {
  private val fields = ArrayBuffer.empty[SerializerField[T,_]]
  private var currentReadMsg: Message = null
  
  protected def _ID(writer: T=>ID) = mkField[ID](writer, { id2Msg(_) })
  protected def _OptionID(writer: T=>Option[ID]) = mkOptionField[ID](writer, { id2Msg(_) })  
  
  protected def _VersionedObject[O](writer: T=>VersionedObject[O]) = {
    mkField[VersionedObject[O]](writer, { o:VersionedObject[O] => id2Msg(o.id) })
  }
  protected def _OptionVersionedObject[O](writer: T=>Option[VersionedObject[O]]) = {
    mkOptionField[VersionedObject[O]](writer, { o:VersionedObject[O] => id2Msg(o.id) })
  }
  protected def _SeqVersionedObject[O](writer: T=>Seq[VersionedObject[O]]) = {
    mkSeqField(writer, { o:VersionedObject[O] => id2Msg(o.id) })   
  }
  
  protected def _Int(writer: T=>Int) = mkField[Int](writer, VarIntMessageField(_))
  
  protected def _Value(writer: T=>Value) = mkField[Value](writer, value2msg)
  protected def _OptionValue(writer: T=>Option[Value]) = mkOptionField[Value](writer, value2msg)
  
  protected def _SeqMessage(writer: T=>Seq[Message]) = mkSeqField(writer, {
    m:Message => MessageField(m)}
  )
  
  private val value2msg = {
    value: Value => value match {
      case StringValue(s) => MessageField(s)
      case IntValue(i) => MessageField(i)
    }
  }
  
  private def mkField[F](writer: T=>F, toMessageField: F=>MessageField) = {
    val f =  new SerializerField(fields.size, writer) {
      def write(obj:T, msg:Message) = msg + (index -> toMessageField(writer(obj)))
    }
    fields += f
    f
  }
  
  private def mkOptionField[F](writer: T=>Option[F], toMessageField: F=>MessageField) = {
    val f =  new SerializerField(fields.size, writer) {
      def write(obj:T, msg:Message) = {
        val value = writer(obj)
        if (value.isDefined) msg + (index -> toMessageField(value.get)) else msg
      }
    }
    fields += f
    f
  }
  
  private def mkSeqField[F](writer: T=>Seq[F], toMessageField: F=>MessageField) = {
    val f =  new SerializerField(fields.size, writer) {
      def write(obj:T, msg:Message) = writer(obj).foldLeft(msg)( (m,v) =>
        m + (index -> toMessageField(v))
      )
    }
    fields += f
    f    
  }
  
  
  private def id2Msg(id:ID) = Fixed128MessageField(id.time, id.node.toLong << 32 | id.seq)
  private val unpackId = { v:(Long,Long) => ID.load((v._2 >> 32).toInt, v._1, (v._2 & 0xFFFFFFFF).toInt) }
  private def msg2Id(f:MessageFields) = f.asDoubleLong map unpackId 
  private def msg2Ids(f:MessageFields) = f.asDoubleLongs map unpackId
  
  def write(obj: T, msg: Message) = {
    fields.foldRight(msg)(_.write(obj, _))
  }

  protected def load: T
  
  implicit def int2v(field: SerializerField[T,Int]) = currentReadMsg(field.index).asInt.get
  implicit def id2v(field: SerializerField[T,ID]) = msg2Id(currentReadMsg(field.index)).get
  implicit def optid2v(field: SerializerField[T,Option[ID]]) = msg2Id(currentReadMsg(field.index))
  implicit def obj2v[O](field: SerializerField[T,VersionedObject[O]]) =  
    VersionedObject(msg2Id(currentReadMsg(field.index)).get)
  implicit def optobj2v[O](field: SerializerField[T,Option[VersionedObject[O]]]) = 
    msg2Id(currentReadMsg(field.index)) map {VersionedObject(_)}
  implicit def seqobj2v[O](field: SerializerField[T,Seq[VersionedObject[O]]]) = 
    msg2Ids(currentReadMsg(field.index)) map {VersionedObject(_)}
  
  implicit def value2v(field: SerializerField[T,Value]) = {
    val msgField = currentReadMsg(field.index)
    msgField.asInt map (Value(_)) getOrElse Value(msgField.asString.get)
  }
  implicit def optvalue2v(field: SerializerField[T,Option[Value]]) = {
    val msgField = currentReadMsg(field.index)
    if (msgField.isEmpty) None else {
      msgField.asInt.map(Value(_)) orElse msgField.asString.map(Value(_))      
    }
  }
  implicit def msgs2v(field: SerializerField[T,Seq[Message]]) = {
    currentReadMsg(field.index).asMessages
  }
  
  def read(msg:Message) = {
    currentReadMsg = msg
    load
  }
}