package org.jlore.core

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.jlore.model._
import org.jlore.model.i18n.Locale

@RunWith(classOf[JUnitRunner])
class BranchTest extends org.jlore.Specification {
  "a branch" should {
    var b = new Branch() 
    "insert commands with earlier IDs before commands with later IDs" in {
      val myLocale = VersionedObject[Locale]()
      val setNameToB = new Locale.SetIdentifier(ID(), myLocale, Some(Value("PropB")))
      b += new Locale.Create(ID(), myLocale)
      b += new Locale.SetIdentifier(ID(), myLocale, Some(Value("PropA")))
      b += setNameToB
      myLocale.in(b).identifier.get.asString must_== "PropA"
    }
  }
  
  "a branch which has a parent" should {
    var parent = new Branch() 
    val myLocale = VersionedObject[Locale]()
    parent += new Locale.Create(ID(), myLocale)
    parent += new Locale.SetIdentifier(ID(), myLocale, Some(Value("myLocale")))
    var b = new Branch (parent)
    "remember the latest values of its parent" in {
      myLocale.in(b) must be(myLocale.in(parent))
    }
    "have changes independent of its parent, until they are merged" in {
      b += new Locale.SetIdentifier(ID(), myLocale, Some(Value("myRenamedLocale")))
      myLocale.in(b) must not be(myLocale.in(parent))
      myLocale.in(b).identifier.get.asString must_== "myRenamedLocale"
      myLocale.in(parent).identifier.get.asString must_== "myLocale"
      
      parent += new Branch.Merge(ID(), b)
      myLocale.in(parent).identifier.get.asString must_== "myRenamedLocale"
      // only equality (objects are re-created for the merged branch)
      myLocale.in(b) must_== myLocale.in(parent) 
    }
  }
}