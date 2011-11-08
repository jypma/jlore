package org.jlore.io.server

import org.jlore.io.ProtocolFactory
import org.jlore.io.Serializer

object ConnectionProtocolFactory extends ProtocolFactory {
  register(1, 1, new Serializer[ConnectionWorker.Push] {
    val branchId = _OptionID(_.branchId) 
    val commandMsgs = _Messages(_.commandMsgs)
    def load = new ConnectionWorker.Push (branchId, commandMsgs)    
  })
}