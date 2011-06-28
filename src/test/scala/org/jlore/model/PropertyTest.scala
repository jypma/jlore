package org.jlore.model

import org.jlore.core._
import org.scalatest.junit.AssertionsForJUnit
import org.junit.Assert._
import org.junit.Test

class PropertyTest extends AssertionsForJUnit {
  @Test def test {
    var b = new Branch()
    val myProp = new VersionedObject(ID.random)
    b += new Property.Create(myProp)
    b += new Property.SetName(myProp, Value("MyProp"))
    assertEquals ("MyProp", b.get[Property](myProp).name.get.asString)
  }
}