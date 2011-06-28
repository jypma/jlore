package org.jlore.model

import org.jlore.core._

case class Instance (
    obj:VersionedObject, 
    values:Map[Property,Value] = Map.empty, 
    relations:Map[Relation,Instance] = Map.empty
) extends ObjectVersion

object Instance {
  class Create (val id:ID) extends Command {
    def run (b: Branch) = new Instance(new VersionedObject(id)) :: Nil
  }
  
  class SetProperty (val i: Instance, val p:Property, n: Option[Value]) extends Command {
    def run (b: Branch) = (n match {
      case Some(value) => i.copy(values = i.values + (p -> value))
      case None => i.copy(values = i.values - p)
    }) :: Nil
  }
}