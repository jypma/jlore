package org.jlore.model.i18n

import org.jlore.core._
import org.jlore.io.Serializer
import org.jlore.model.Value

case class Locale (
    obj:VersionedObject[Locale], 
    identifier: Option[Value] = None
) extends ObjectVersion[Locale] 

object Locale {
  case class Create (id:ID, obj:VersionedObject[Locale]) extends ChangeCommand {
    def change (b: Branch) = new Locale(obj) :: Nil
  }

  case class SetIdentifier (id:ID, obj:VersionedObject[Locale], identifier: Option[Value]) extends ChangeCommand {
    def change (b: Branch) = {
      b.get[Locale](obj).copy(identifier=identifier) :: Nil
    }
  }
}