package org.jlore.io.server
import org.jlore.logging.Log

object ServerMain extends App with Log {
  val server = new Server (9090)
  server.start
  Thread.sleep(20000)
  log.info("Time's up. Stopping server.")
  server.stop
}