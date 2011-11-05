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
  
  case class SetProperty (id:ID, i: VersionedObject[Instance], 
      p:VersionedObject[Property], n: Option[Value]) extends ChangeCommand {
    def change (b: Branch) = {
      val instance = b.get[Instance](i)
      (n match {
        case Some(value) => instance.copy(values = instance.values + (p -> value))
        case None => instance.copy(values = instance.values - p)
      }) :: Nil
    }
  }
}