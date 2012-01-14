package org.jlore.io.server

import org.jlore.io.ProtocolFactory
import org.jlore.io.Serializer
import org.jlore.core.CommandProtocolFactory

class SystemProtocolFactory extends ProtocolFactory {
  val commandProtocolFactory = new CommandProtocolFactory
  
  register(1, 1, new Serializer[System.Push] {
    val commandProtocol = commandProtocolFactory.instantiate()
    
    val branchId = _OptionID(_.branchId) 
    val commandMsgs = _SeqMessage(p => commandProtocol.write(p.commands))
    def load = new System.Push (branchId, commandProtocol.read(commandMsgs))    
  })
}