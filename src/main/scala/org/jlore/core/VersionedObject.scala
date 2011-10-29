package org.jlore.core

class VersionedObject[+T] private (
  val id: ID
) extends Identifyable {
  def in(b: Branch) = b.get[T](this)  
}

object VersionedObject {
  def apply[T]() = new VersionedObject[T](ID())
  
  // TODO cache loaded instances these somewhere, might be loading same
  // versionedobject ID twice
  def apply[T](id:ID) = new VersionedObject[T](id)
}