package org.jlore.model

import org.jlore.core._
import org.jlore.io.Serializer

case class Property (
    obj:VersionedObject[Property], 
    name: Option[Value] = None
) extends ObjectVersion[Property] 

object Property {
  class Create (id:ID, val obj:VersionedObject[Property]) extends ChangeCommand(id) {
    def change (b: Branch) = new Property(obj) :: Nil
  }

  CommandProtocolFactory.register(1, 1, new Serializer[Create] {
    val id = _ID(_.id) 
    val obj = _VersionedObject[Property](_.obj)
    def load = new Create (id, obj)    
  })
  
  class SetName (id:ID, val p:VersionedObject[Property], n: Value) extends ChangeCommand(id) {
    def change (b: Branch) = {
      b.get[Property](p).copy(name=Some(n)) :: Nil
    }
  }
}