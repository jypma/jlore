package org.jlore.io

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.jlore.model._
import org.specs2.mutable.Specification

@RunWith(classOf[JUnitRunner])
class ProtocolTest extends org.jlore.Specification{
  "protocols when registered in a protocol factory" should {
    val factory = new ProtocolFactory()
    "write and read a class with an int field" in {
      factory.register(1, 1, new Serializer[WithInt] {
        val i = field[Int](_.i) 
        def load = new WithInt (i)    
      })
      val protocol = factory.instantiate
      1 must_== 1
    }
    
  }
  
  case class WithInt (val i: Int)
}