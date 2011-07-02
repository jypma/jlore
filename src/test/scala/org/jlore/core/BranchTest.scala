package org.jlore.core

import org.junit.runner.RunWith
import org.specs.runner.JUnitSuiteRunner
import org.specs.SpecificationWithJUnit
import org.jlore.model._

@RunWith(classOf[JUnitSuiteRunner])
class BranchTest extends org.jlore.Specification {
  "a branch" should {
    var b = new Branch() 
    "insert commands with earlier IDs before commands with later IDs" in {
      val myProp = new VersionedObject[Property](ID())
      val setNameToB = new Property.SetName(ID(), myProp, Value("PropB"))
      b += new Property.Create(ID(), myProp)
      b += new Property.SetName(ID(), myProp, Value("PropA"))
      b += setNameToB
      myProp.in(b).name.get.asString must_== "PropA"
    }
  }
}