package org.jlore.model

import org.jlore.core._

case class Property (obj:VersionedObject, name: Option[Value] = None) extends ObjectVersion {
}

object Property {
  class Create (val obj:VersionedObject) extends Command {
    def run (b: Branch) = new Property(obj) :: Nil
  }
  
  class SetName (val p:VersionedObject, n: Value) extends Command {
    def run (b: Branch) = {
      b.get[Property](p).copy(name=Some(n)) :: Nil
    }
  }
}