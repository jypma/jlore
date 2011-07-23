package org.jlore.model

import org.jlore.core._

case class Property (
    obj:VersionedObject[Property], 
    name: Option[Value] = None
) extends ObjectVersion[Property] 

object Property {
  class Create (id:ID, val obj:VersionedObject[Property]) extends ChangeCommand(id) {
    def change (b: Branch) = new Property(obj) :: Nil
  }
  
  class SetName (id:ID, val p:VersionedObject[Property], n: Value) extends ChangeCommand(id) {
    def change (b: Branch) = {
      b.get[Property](p).copy(name=Some(n)) :: Nil
    }
  }
}