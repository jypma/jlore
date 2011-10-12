package org.jlore.io

trait ByteWriter[T] {
  def << (buf: Array[Byte]): T
  def << (byte: Byte): T
}