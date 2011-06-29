package org.jlore.model

import org.jlore.core._

case class Relation(
    val obj:VersionedObject[Relation], 
    val name: Option[Value],
    val plural: Option[Value],
    val other: VersionedObject[Relation]
) extends ObjectVersion[Relation]

object Relation {
  class Create(val endA: VersionedObject[Relation], endB: VersionedObject[Relation]) extends Command {
    def run (b: Branch) = new Relation(endA, None, None, endB) ::
                          new Relation(endB, None, None, endA) :: Nil
  }

  class SetName(val r: VersionedObject[Relation], n: Value) extends Command {
    def run (b: Branch) = b.get[Relation](r).copy(name = Some(n)) :: Nil
  }
}