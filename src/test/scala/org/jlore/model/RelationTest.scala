package org.jlore.model

import org.jlore.core._
import org.scalatest.junit.AssertionsForJUnit
import org.junit.Assert._
import org.junit.Test

class RelationTest extends AssertionsForJUnit {
  @Test def test {
    var b = new Branch()
    val parent = new VersionedObject(ID.random)
    val child = new VersionedObject(ID.random)
    b += new Relation.Create(parent, child)
    var relation = b.get[Relation](parent)
    assertNotNull (relation.other)
    assertSame (relation.other.other, relation)
    
    b += new Relation.SetName(parent, Value("Parent"))
    relation = b.get[Relation](parent)
    assertEquals ("Parent", relation.name.get.asString)
    assertNotNull (relation.other)
    
    b += new Relation.SetName(relation.other, Value("Child"))
    relation = b.get[Relation](child)
    assertEquals ("Child", relation.name.get.asString)
    assertEquals ("Parent", relation.other.name.get.asString)
  }
}