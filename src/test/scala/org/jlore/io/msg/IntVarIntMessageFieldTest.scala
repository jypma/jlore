package org.jlore.io.msg

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.jlore.io.ByteBuffer
import org.specs2.specification.Example

@RunWith(classOf[JUnitRunner])
class IntVarIntMessageFieldTest extends org.jlore.Specification {
  "an int lower than 127" should test(126, 1) ;
  "an int 127" should test(127, 1) ;
  "an int 128" should test(128, 2) ;
  "an big integer" should test(Int.MaxValue-1, 5) ;
  
  def test(testValue: Int, expectedLength: Int):Example = { 
    {
      val i = new IntVarIntMessageField(testValue)
      "give its value when asked" in {
        log.debug("" + Int.MaxValue)
        i.asInt must_== Some(testValue)
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
        val read = IntVarIntMessageField(bytes)
        read.asInt.get must_== i.asInt.get
      }
    }
  }
}