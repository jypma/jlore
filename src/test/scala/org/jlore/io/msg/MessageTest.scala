package org.jlore.io.msg
import org.jlore.io.ByteBuffer
import org.specs2.specification.Example

class MessageTest extends org.jlore.Specification {
  "an empty message" should beOk(
      new Message(1,2)
  )
  "a message with a field" should beOk(
      new Message(1,2) +
      (3 -> new IntVarIntMessageField(4))
  )
  
  def beOk(m: Message):Example = { 
    "be writeable and readable" in {
      val buf = new ByteBuffer
      m.encodeTo(buf)
      log.debug("Encoded message: " + buf)
      buf.size must be_>(0)
      val readMessage = Message(buf)
      readMessage must beSome
      val m2 = readMessage.get
      m2.msg === m.msg
      m2.version === m.version
      log.debug("m" + m.fields.size)
      log.debug("m2" + m2.fields.size)
      m2.fields.size === m.fields.size
    }  
  }
  
}