package org.jlore.model

import org.jlore.core._
import org.scalatest.junit.AssertionsForJUnit
import org.junit.Assert._
import org.junit.Test

class RelationTest extends AssertionsForJUnit {
  @Test def test {
    var b = new Branch()
    val parent = new VersionedObject[Relation](ID.random)
    val child = new VersionedObject[Relation](ID.random)
    b += new Relation.Create(parent, child)
    /*
    var relation = parent.in(b)
    assertNotNull (relation.other)
    assertSame (child, relation.other)
    assertSame (parent, relation.other.in(b).other)
    
    b += new Relation.SetName(parent, Value("Parent"))
    relation = parent.in(b)
    assertEquals ("Parent", relation.name.get.asString)
    assertNotNull (relation.other)
    
    b += new Relation.SetName(relation.other, Value("Child"))
    relation = child.in(b)
    assertEquals ("Child", relation.name.get.asString)
    assertEquals ("Parent", relation.other.in(b).name.get.asString)
    */
  }
}