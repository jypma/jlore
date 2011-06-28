package org.jlore.core

trait Identifyable {
  val id: ID
  
  override def hashCode = id.hashCode
  
  //override def toString = getClass().getName() + "@" + super.hashCode
}