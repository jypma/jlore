package org.jlore.io.msg
import org.jlore.io.ByteBuffer
import org.specs2.specification.Example

class MessageTest extends org.jlore.Specification {
  "an empty message" should {
    val m = new Message(1,2)
    "be writeable and readable" in {
      val buf = new ByteBuffer
      m.encodeTo(buf)
      buf.size must be_>(0)
      val readMessage = Message(buf)
      readMessage must beSome
      val m2 = readMessage.get
      m2.msg === m.msg
      m2.version === m.version
      m2.fields.size === 0
    }    
  }
      
  "a message with a field" should {
    val m = new Message(1,2) + (3 -> MessageField(4))
    "come back with the field intact" in {
      val buf = new ByteBuffer
      m.encodeTo(buf) 
      val m2 = Message(buf).get
      m2(3).asInt === Some(4)
      m2(3).asInts === 4 :: Nil
    }
  }
  
  "a message with two values for the same field" should {
    val m = new Message(1,2) + (3 -> MessageField(4)) +
                               (3 -> MessageField(5))
    "list both values" in {
      m(3).asInts must contain (4, 5).only
    }
    "come back with both values" in {
      val buf = new ByteBuffer
      m.encodeTo(buf) 
      val m2 = Message(buf).get
      m2(3).asInts must contain (4, 5).only
    }
  }
  
  "an embedded message" should {
    val m = new Message(1,2) + (3 -> MessageField(
      new Message(4,5) + (6 -> MessageField(7))
    ))
    "come back intact" in {
      val buf = new ByteBuffer
      m.encodeTo(buf) 
      log.debug("encoded to " + buf)
      val m2 = Message(buf).get
      m2(3).asMessage.get(6).asInt === Some(7)
    }
  }
  
}