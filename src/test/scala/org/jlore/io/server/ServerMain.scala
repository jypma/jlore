package org.jlore.io.server

object ServerMain extends App {
  val server = new Server (9090)
  server.start
  Thread.sleep(120000)
  server.stop
}