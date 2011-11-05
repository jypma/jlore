package org.jlore.model

import org.jlore.core._

class PropertyTest extends org.jlore.Specification {
  "properties in branches" should {
    var b = new Branch()
    "be creatable and have their name set" in {
      val myProp = VersionedObject[Property]()
      b += new Property.Create(ID(), myProp)
      b += new Property.SetName(ID(), myProp, Value("MyProp"))
      myProp.in(b).name.get.asString must_== "MyProp"
    }
  }
  
  "a property creation command" should {
    val myProp = VersionedObject[Property]()
    val c = new Property.Create(ID(), myProp)
    val p = CommandProtocolFactory.instantiate()
    "be writeable and readable" in {
      val msg = p.write(c)
      msg must beSome
      val readResult = p.read[Property.Create](msg.get)
      readResult must beSome
      readResult.get must_== c
    }
  }
}