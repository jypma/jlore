package org.jlore.model

import org.jlore.core._

case class Instance (
    obj:VersionedObject[Instance], 
    values:Map[VersionedObject[Property],Value] = Map.empty, 
    relations:Map[VersionedObject[Relation],VersionedObject[Instance]] = Map.empty
) extends ObjectVersion[Instance]

object Instance {
  class Create (val obj: VersionedObject[Instance]) extends Command {
    def run (b: Branch) = new Instance(obj) :: Nil
  }
  
  class SetProperty (val i: VersionedObject[Instance], val p:VersionedObject[Property], n: Option[Value]) extends Command {
    def run (b: Branch) = {
      val instance = b.get[Instance](i)
      (n match {
        case Some(value) => instance.copy(values = instance.values + (p -> value))
        case None => instance.copy(values = instance.values - p)
      }) :: Nil
    }
  }
}