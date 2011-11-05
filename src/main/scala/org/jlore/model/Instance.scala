package org.jlore.model

import org.jlore.core._

case class Instance (
    obj:VersionedObject[Instance], 
    values:Map[VersionedObject[Property],Value] = Map.empty, 
    relations:Map[VersionedObject[Relation],VersionedObject[Instance]] = Map.empty
) extends ObjectVersion[Instance]

object Instance {
  case class Create (id:ID, obj: VersionedObject[Instance]) extends ChangeCommand {
    def change (b: Branch) = new Instance(obj) :: Nil
  }
  
  case class SetProperty (id:ID, instance: VersionedObject[Instance], 
      property:VersionedObject[Property], value: Option[Value]) extends ChangeCommand {
    def change (b: Branch) = {
      val i = b.get[Instance](instance)
      (value match {
        case Some(v) => i.copy(values = i.values + (property -> v))
        case None => i.copy(values = i.values - property)
      }) :: Nil
    }
  }
}