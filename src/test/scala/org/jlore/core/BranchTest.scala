package org.jlore.core

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.jlore.model._

@RunWith(classOf[JUnitRunner])
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
  
  "a branch which has a parent" should {
    var parent = new Branch() 
    val myProp = new VersionedObject[Property](ID())
    parent += new Property.Create(ID(), myProp)
    parent += new Property.SetName(ID(), myProp, Value("myProp"))
    var b = new Branch (parent)
    "remember the latest values of its parent" in {
      myProp.in(b) must be(myProp.in(parent))
    }
    "have changes independent of its parent, until they are merged" in {
      b += new Property.SetName(ID(), myProp, Value("myRenamedProp"))
      myProp.in(b) must not be(myProp.in(parent))
      myProp.in(b).name.get.asString must_== "myRenamedProp"
      myProp.in(parent).name.get.asString must_== "myProp"
      
      parent += new Branch.Merge(ID(), b)
      myProp.in(parent).name.get.asString must_== "myRenamedProp"
      // only equality (objects are re-created for the merged branch)
      myProp.in(b) must_== myProp.in(parent) 
    }
  }
}