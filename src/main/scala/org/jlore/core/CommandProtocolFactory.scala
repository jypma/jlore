package org.jlore.core

import org.jlore.io.ProtocolFactory
import org.jlore.model._
import org.jlore.io.Serializer

object CommandProtocolFactory extends ProtocolFactory {
    register(1, 1, new Serializer[Property.Create] {
      val id = _ID(_.id) 
      val property = _VersionedObject[Property](_.property)
      def load = new Property.Create (id, property)    
    })
    register(2, 1, new Serializer[Property.SetName] {
      val id = _ID(_.id) 
      val property = _VersionedObject[Property](_.property)
      val name = _Value(_.name)
      def load = new Property.SetName (id, property, name)    
    })    
}