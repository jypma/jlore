package org.jlore.model.i18n

import org.jlore.core._
import org.jlore.io.Serializer
import org.jlore.model.Value

case class Name (
    obj:VersionedObject[Name], 
    locale: VersionedObject[Locale],
    plural: VersionedObject[Plural],
    localName: Option[Value] = None
) extends ObjectVersion[Name] 

object Name {
  case class Create (id:ID, obj:VersionedObject[Name], locale: VersionedObject[Locale],
                     plural: VersionedObject[Plural]) extends ChangeCommand {
    def change (b: Branch) = new Name(obj, locale, plural) :: Nil
  }

  case class SetLocalName (id:ID, obj:VersionedObject[Name], name: Option[Value]) extends ChangeCommand {
    def change (b: Branch) = {
      b.get[Name](obj).copy(localName=name) :: Nil
    }
  }
}