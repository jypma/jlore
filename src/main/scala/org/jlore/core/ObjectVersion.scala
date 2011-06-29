package org.jlore.core

trait ObjectVersion[+T] {
  val obj: VersionedObject[T]
}