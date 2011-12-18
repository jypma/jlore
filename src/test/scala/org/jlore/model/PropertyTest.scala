package org.jlore.model

import org.jlore.core._
import org.jlore.model.i18n.Name

class PropertyTest extends org.jlore.Specification {
  "properties in branches" should {
    val bootstrap = new BranchBootstrap()
    import bootstrap._
    
    "be creatable and have their name set and changed" in {
      val myProp = VersionedObject[Property]()
      b += new Property.Create(ID(), myProp)
      val name = single("MyProp")
      b += new Property.AddNames(ID(), myProp, name :: Nil)
      myProp.in(b).names(0).in(b).localName === Some(Value("MyProp"))
      b += new Name.SetLocalName(ID(), name, Some(Value("MyChangedProp")))
      myProp.in(b).names(0).in(b).localName === Some(Value("MyChangedProp"))
    }
  }
  
  "a property creation command" should {
    val myProp = VersionedObject[Property]()
    val c = new Property.Create(ID(), myProp)
    val bootstrap = new BranchBootstrap()
    import bootstrap._
    val n = new Property.AddNames(ID(), myProp, ("MyProp","MyProps"))
    val p = CommandProtocolFactory.instantiate()
    "be writeable and readable" in {
      val msg = p.write(c)
      msg must beSome
      val readResult = p.read[Property.Create](msg.get)
      readResult must beSome
      readResult.get must_== c
      
      p.read[Property.AddNames](p.write(n).get).get must_== n
    }
  }
}