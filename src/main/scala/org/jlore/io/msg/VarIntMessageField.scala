package org.jlore.io.msg

class VarIntMessageField(val bytes:Array[Byte]) {
  import VarIntMessageField._
  
  def toInt = {
    if (bytes.length > 5) None else
    if (bytes.length == 5 && ((bytes(0) | 0xF0) > 0)) None else {
      Some(bytes.length match {
        case 5 => (bytes(0) & clearMSB) << 28 |
                  (bytes(1) & clearMSB) << 21 |
                  (bytes(2) & clearMSB) << 14 |
                  (bytes(3) & clearMSB) << 7  | bytes(4)
        case 4 => (bytes(0) & clearMSB) << 21 |
                  (bytes(1) & clearMSB) << 14 |
                  (bytes(2) & clearMSB) << 7  | bytes(3)
        case 3 => (bytes(0) & clearMSB) << 14 |
                  (bytes(1) & clearMSB) << 7  | bytes(2)
        case 2 => (bytes(0) & clearMSB) << 7  | bytes(1)
        case 1 =>  bytes(0) 
      })
    }
  }
}

object VarIntMessageField {
  private val MSB = Byte.MinValue      // 1000 0000 
  private val clearMSB = Byte.MaxValue // 0111 1111
  
  def apply(i: Int) = {
    val byte5 = ((i >> 28) & 0x0F).toByte 
    val byte4 = ((i >> 21) & 0x7F).toByte 
    val byte3 = ((i >> 14) & 0x7F).toByte
    val byte2 = ((i >> 7)  & 0x7F).toByte
    val byte1 = ( i        & 0x7F).toByte
    if (byte5 != 0) {
      new VarIntMessageField(Array(
          (byte5 | MSB).toByte, 
          (byte4 | MSB).toByte, 
          (byte3 | MSB).toByte, 
          (byte2 | MSB).toByte, byte1))
    } else if (byte4 != 0) {
      new VarIntMessageField(Array(
          (byte4 | MSB).toByte, 
          (byte3 | MSB).toByte, 
          (byte2 | MSB).toByte, byte1))      
    } else if (((i >> 14) & 0xFF) > 0) {
      new VarIntMessageField(Array(
          (byte3 | MSB).toByte, 
          (byte2 | MSB).toByte, byte1))            
    } else if (((i >> 7) & 0xFF) > 0) {
      new VarIntMessageField(Array(
          (byte2 | MSB).toByte, byte1))                  
    } else {
      new VarIntMessageField(Array(byte1))
    }
  }
}