package org.jlore.model

import org.jlore.core._

case class Relation(
    val obj:VersionedObject[Relation], 
    val name: Option[Value],
    val plural: Option[Value],
    val other: VersionedObject[Relation]
) extends ObjectVersion[Relation]

object Relation {
  case class Create(id:ID, endA: VersionedObject[Relation], 
                           endB: VersionedObject[Relation]) extends ChangeCommand {
    def change (b: Branch) = new Relation(endA, None, None, endB) ::
                             new Relation(endB, None, None, endA) :: Nil
  }

  case class SetName(id:ID, r: VersionedObject[Relation], n: Value) extends ChangeCommand {
    def change (b: Branch) = b.get[Relation](r).copy(name = Some(n)) :: Nil
  }
}