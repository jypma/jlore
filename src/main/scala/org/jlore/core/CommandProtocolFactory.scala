package org.jlore.core

import org.jlore.io.ProtocolFactory
import org.jlore.model._
import org.jlore.io.Serializer
import org.jlore.model.i18n.Name

class CommandProtocolFactory extends ProtocolFactory {
  register(1, 1, new Serializer[Property.Create] {
    val id = _ID(_.id) 
    val property = _VersionedObject[Property](_.property)
    def load = new Property.Create (id, property)    
  })
  register(2, 1, new Serializer[Property.AddNames] {
    val id = _ID(_.id) 
    val property = _VersionedObject[Property](_.property)
    val names = _SeqVersionedObject[Name](_.names)
    def load = new Property.AddNames (id, property, names)    
  })    
  register(3, 1, new Serializer[Instance.Create] {
    val id = _ID(_.id) 
    val obj = _VersionedObject[Instance](_.obj)
    def load = new Instance.Create (id, obj)    
  })
  register(4, 1, new Serializer[Instance.SetProperty] {
    val id = _ID(_.id) 
    val instance = _VersionedObject[Instance](_.instance)
    val property = _VersionedObject[Property](_.property)
    val value = _OptionValue(_.value)
    def load = new Instance.SetProperty (id, instance, property, value)    
  })
}