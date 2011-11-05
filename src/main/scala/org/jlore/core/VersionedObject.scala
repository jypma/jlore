package org.jlore.core

case class VersionedObject[+T] (
  val id: ID = ID()
) extends Identifyable {
  def in(b: Branch) = b.get[T](this)  
}


object VersionedObject {
  /*
  def apply[T]() = new VersionedObject[T](ID())
  
  // TODO cache loaded instances these somewhere, might be loading same
  // versionedobject ID twice
  def load[T](id:ID) = new VersionedObject[T](id)
  */
}
