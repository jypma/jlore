package org.jlore.model

import org.jlore.core._
import org.jlore.io.Serializer
import org.jlore.model.i18n.Name

case class Property (
    obj:VersionedObject[Property], 
    names: Seq[VersionedObject[Name]] = Nil
) extends ObjectVersion[Property] 

object Property {
  case class Create (id:ID, property:VersionedObject[Property]) extends ChangeCommand {
    def change (b: Branch) = new Property(property) :: Nil
  }

  case class AddNames (id:ID, property:VersionedObject[Property], names: Seq[VersionedObject[Name]]) extends ChangeCommand {
    def change (b: Branch) = {
      val p = b.get[Property](property)
      p.copy(names = names ++: p.names) :: Nil
    }
  }
}