package org.jlore.core

class VersionedObject[+T](
  val id: ID
) extends Identifyable {
  def in(b: Branch) = b.get[T](this)  
}