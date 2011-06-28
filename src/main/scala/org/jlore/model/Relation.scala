package org.jlore.model

import org.jlore.core._

class Relation(
    val obj:VersionedObject, 
    val name: Option[Value],
    val plural: Option[Value],
    otherObj:Option[VersionedObject],
    otherName: Option[Value],
    otherPlural: Option[Value],
    o: Option[Relation]
) extends ObjectVersion {
  def this (obj: VersionedObject, otherObj: VersionedObject) =
    this (obj, None, None, Some(otherObj), None, None, None)
  
  val other = o match {
    case Some(r) => r
    case None => new Relation (otherObj.get, otherName, otherPlural, None, None, None, Some(this))
  }
  
  def copy (name: Option[Value] = name, plural: Option[Value] = plural) = {
    new Relation (obj, name, plural, Some(other.obj), other.name, other.plural, None)
  }
}

object Relation {
  class Create(val objA: VersionedObject, objB: VersionedObject) extends Command {
    def run (b: Branch) = new Relation(objA, objB) :: Nil
  }

  class SetName(val r: VersionedObject, n: Value) extends Command {
    def run (b: Branch) = b.get[Relation](r).copy(name = Some(n)) :: Nil
  }
}