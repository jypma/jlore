package org.jlore.logging
import java.text.SimpleDateFormat

object Appender {
  val fmt = new SimpleDateFormat("dd/MM HH:mm:ss.SSS")
  def out(name: String, level: LogLevel, msg: AnyRef) {
    val now = fmt.format(System.currentTimeMillis)
    val thread = Thread.currentThread.getName
    println("%s %5s [%-10.10s] %s - %s".format(now, level, thread, name, msg))
  }
}