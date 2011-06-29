package org.jlore.model

import org.jlore.core._

case class Property (
    obj:VersionedObject[Property], 
    name: Option[Value] = None
) extends ObjectVersion[Property] 

object Property {
  class Create (val obj:VersionedObject[Property]) extends Command {
    def run (b: Branch) = new Property(obj) :: Nil
  }
  
  class SetName (val p:VersionedObject[Property], n: Value) extends Command {
    def run (b: Branch) = {
      b.get[Property](p).copy(name=Some(n)) :: Nil
    }
  }
}