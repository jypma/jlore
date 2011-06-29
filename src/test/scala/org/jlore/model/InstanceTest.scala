package org.jlore.model

import org.jlore.core._
import org.scalatest.junit.AssertionsForJUnit
import org.junit.Assert._
import org.junit.Test

class InstanceTest extends AssertionsForJUnit {
  @Test def test() { 
    var b = new Branch()
    val i = new VersionedObject[Instance](ID.random)
    b += new Instance.Create(i)
    val myProp = new VersionedObject[Property](ID.random)
    b += new Property.Create(myProp)
    b += new Property.SetName(myProp, Value("MyProp"))
    b += new Instance.SetProperty(i, myProp, Some(Value("MyValue")))
    println (i.in(b).values)
  }
}