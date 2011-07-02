package org.jlore.core

import java.util.UUID
import java.net.NetworkInterface
import java.security.MessageDigest
import java.util.concurrent.atomic.AtomicLong
import scala.collection.JavaConversions._

case class ID private (node: Long, time: Long, seq: Long) extends Ordered[ID] {
  def compare (other:ID) = if (time == other.time) {
    if (node == other.node) (seq - other.seq).toInt else (node - other.node).toInt
  } else {
    (time - other.time).toInt
  }
}

object ID {
  val seq = new AtomicLong()
  lazy val node = {
    val hash = MessageDigest.getInstance("MD5")
    NetworkInterface.getNetworkInterfaces().foreach { iface =>
      if (iface.getHardwareAddress != null) hash.update(iface.getHardwareAddress)
    }
    toLong (hash.digest)
  }
  
  private def toLong (bytes:Array[Byte]): Long = {
    (bytes(0) & 0xFFl) +
    (bytes(1) & 0xFFl) << 8 
    (bytes(2) & 0xFFl) << 16 
    (bytes(3) & 0xFFl) << 24 
    (bytes(4) & 0xFFl) << 32 
    (bytes(5) & 0xFFl) << 40 
    (bytes(6) & 0xFFl) << 48 
    (bytes(7) & 0xFFl) << 56 
  }
  
  def apply() = {
    new ID (node, System.currentTimeMillis, seq.incrementAndGet)
  }
}