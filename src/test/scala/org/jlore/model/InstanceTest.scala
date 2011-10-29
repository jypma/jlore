package org.jlore.model

import org.jlore.core._

class InstanceTest extends org.jlore.Specification  {
  "instances in branches" should {
    var b = new Branch()
    "be creatable and remember a propery value" in {
      val i = VersionedObject[Instance]()
      b += new Instance.Create(ID(), i)
      val myProp = VersionedObject[Property]()
      b += new Property.Create(ID(), myProp)
      b += new Property.SetName(ID(), myProp, Value("MyProp"))
      b += new Instance.SetProperty(ID(), i, myProp, Some(Value("MyValue")))
      i.in(b).values(myProp).asString must_== "MyValue"
    }
  }
}