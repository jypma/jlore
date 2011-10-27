package org.jlore.io.msg

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.jlore.io.ByteBuffer
import org.specs2.specification.Example

@RunWith(classOf[JUnitRunner])
class LongVarIntMessageFieldTest extends org.jlore.Specification {
  "zero" should test(0, 1) ;
  "a small long" should test(127, 1) ;
  "a moderate long" should test(256, 2) ;
  "a big long" should test(Long.MaxValue, 9) ;
  "a huge long" should test(Long.MinValue, 10) ;
  
  def test(testValue: Long, expectedLength: Int):Example = { 
    {
      val i = new LongVarIntMessageField(testValue)
      "give its value when asked" in {
        i.asLong must_== Some(testValue)
      }
      "result in " + expectedLength + " bytes of output" in {
        val buf = new ByteBuffer()
        i.encodeTo(buf)
        buf.size must_== expectedLength
      }
      "give the same int back in roundtrip" in {
        val buf = new ByteBuffer()
        i.encodeTo(buf)
        log.debug("Encoded: " + buf)
        val bytes = buf.read(buf.size).get
        val read = LongVarIntMessageField(bytes)
        read.asLong.get must_== i.asLong.get
      }
    }
  }
}