package org.jlore.model

import org.jlore.core._

class InstanceTest extends org.jlore.Specification  {
  "instances in branches" should {
    val bootstrap = new BranchBootstrap()
    import bootstrap._
    
    "be creatable and remember a propery value" in {
      val i = VersionedObject[Instance]()
      b += new Instance.Create(ID(), i)
      val myProp = VersionedObject[Property]()
      b += new Property.Create(ID(), myProp)
      b += new Property.AddNames(ID(), myProp, ("value","values"))
      b += new Instance.SetProperty(ID(), i, myProp, Some(Value("MyValue")))
      i.in(b).values(myProp).asString must_== "MyValue"
    }
  }
  
  "instance commands" should {
      val i = VersionedObject[Instance]()
      val p = VersionedObject[Property]()
      val createP = new Property.Create(ID(), p)
      val createI = new Instance.Create(ID(), i)
      val setP = new Instance.SetProperty(ID(), i, p, Some(Value("MyValue")))
      val f = CommandProtocolFactory.instantiate()
      "be writeable and readable" in {
        val msg = f.write(setP)
        msg must beSome
        val readResult = f.read[Instance.SetProperty](msg.get)
        readResult must beSome
        readResult.get must_== setP
      }
  }
  
  "instance commands with a null value" should {
      val i = VersionedObject[Instance]()
      val p = VersionedObject[Property]()
      val createP = new Property.Create(ID(), p)
      val createI = new Instance.Create(ID(), i)
      val setP = new Instance.SetProperty(ID(), i, p, None)
      val f = CommandProtocolFactory.instantiate()
      "be writeable and readable" in {
        val msg = f.write(setP)
        msg must beSome
        val readResult = f.read[Instance.SetProperty](msg.get)
        readResult must beSome
        readResult.get must_== setP
      }
  }
  
}