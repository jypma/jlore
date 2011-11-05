package org.jlore.model

import org.jlore.core._
import org.jlore.io.Serializer

case class Property (
    obj:VersionedObject[Property], 
    name: Option[Value] = None
) extends ObjectVersion[Property] 

object Property {
  case class Create (id:ID, property:VersionedObject[Property]) extends ChangeCommand {
    def change (b: Branch) = new Property(property) :: Nil
  }

  case class SetName (id:ID, property:VersionedObject[Property], name: Value) extends ChangeCommand {
    def change (b: Branch) = {
      b.get[Property](property).copy(name=Some(name)) :: Nil
    }
  }
}