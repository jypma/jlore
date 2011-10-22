package org.jlore.model

import org.jlore.core._

class RelationTest extends org.jlore.Specification  {
  "relations in a branch" should {
    var b = new Branch()
    "be creatable and hold a name on both ends" in {
      val parent = new VersionedObject[Relation](ID())
      val child = new VersionedObject[Relation](ID())
      b += new Relation.Create(ID(), parent, child)
    
      parent.in(b).other must not beNull;
      parent.in(b).other must be(child)
      parent.in(b).other.in(b).other must be(parent)
    
      b += new Relation.SetName(ID(), parent, Value("Parent"))
      parent.in(b).name.get.asString must_== "Parent"
      parent.in(b).other must not beNull;
      parent.in(b).other must be(child)
    
      b += new Relation.SetName(ID(), child, Value("Child"))
      child.in(b).name.get.asString must be matching ".*ild"
      child.in(b).other.in(b).name.get.asString must_== "Parent"
    }
    
  }
}
