package org.jlore.model.i18n

import org.jlore.core._
import org.jlore.io.Serializer
import org.jlore.model.Value

case class Plural (
    obj:VersionedObject[Plural], 
    localName: Option[Value] = None
) extends ObjectVersion[Plural] 

object Plural {
  case class Create (id:ID, obj:VersionedObject[Plural]) extends ChangeCommand {
    def change (b: Branch) = new Plural(obj) :: Nil
  }

  case class SetLocalName (id:ID, obj:VersionedObject[Plural], name: Option[Value]) extends ChangeCommand {
    def change (b: Branch) = {
      b.get[Plural](obj).copy(localName=name) :: Nil
    }
  }
}