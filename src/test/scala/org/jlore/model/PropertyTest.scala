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
}