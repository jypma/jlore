package org.jlore.io

import org.junit.runner.RunWith
import org.specs.runner.JUnitSuiteRunner
import org.jlore.model._

@RunWith(classOf[JUnitSuiteRunner])
class ProtocolTest extends org.jlore.Specification{
  "protocols when registered in a protocol factory" should {
    val factory = new ProtocolFactory()
    "write and read a class with an int field" in {
      factory.register(1, 1, new Serializer[WithInt] {
        val i = field[Int](_.i) 
        def load = new WithInt (i)    
      })
      val protocol = factory.instantiate
      
    }
    
  }
  
  case class WithInt (val i: Int)
}