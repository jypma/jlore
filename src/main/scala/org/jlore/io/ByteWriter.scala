package org.jlore.io

trait ByteWriter[T] {
  def << (buf: Seq[Byte]): T
  def << (byte: Byte): T
}