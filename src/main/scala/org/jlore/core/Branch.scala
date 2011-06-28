package org.jlore.core

class Branch (
  val commands: Seq[Command] = Nil,
  val latestVersions: Map[VersionedObject,ObjectVersion] = Map.empty
) {
  def + (c: Command) = {
    val newObjs = c.run(this)
    val newVersions = latestVersions ++ (newObjs map { v => (v.obj -> v) })
    new Branch (commands :+ c, newVersions)
  }
  def get [T <: ObjectVersion: Manifest] (obj: VersionedObject): T = {
    latestVersions(obj).asInstanceOf[T]
  }
}