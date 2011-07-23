package org.jlore.model

import org.jlore.core._

case class Instance (
    obj:VersionedObject[Instance], 
    values:Map[VersionedObject[Property],Value] = Map.empty, 
    relations:Map[VersionedObject[Relation],VersionedObject[Instance]] = Map.empty
) extends ObjectVersion[Instance]

object Instance {
  class Create (id:ID, val obj: VersionedObject[Instance]) extends ChangeCommand(id) {
    def change (b: Branch) = new Instance(obj) :: Nil
  }
  
  class SetProperty (id:ID, val i: VersionedObject[Instance], 
      val p:VersionedObject[Property], n: Option[Value]) extends ChangeCommand(id) {
    def change (b: Branch) = {
      val instance = b.get[Instance](i)
      (n match {
        case Some(value) => instance.copy(values = instance.values + (p -> value))
        case None => instance.copy(values = instance.values - p)
      }) :: Nil
    }
  }
}