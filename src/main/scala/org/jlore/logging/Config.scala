package org.jlore.logging
import LogTree._
import LogLevel._
import org.jlore.io.msg.Message

object Config {
       "org.jlore" |- DEBUG
  classOf[Message] |- WARN  
}