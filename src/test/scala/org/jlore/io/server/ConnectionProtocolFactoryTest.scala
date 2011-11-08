package org.jlore.io.server
import org.jlore.io.msg.Message
import org.jlore.io.msg.MessageField

class ConnectionProtocolFactoryTest extends org.jlore.Specification {
  val myMsg1 = Message(10,20) + (30 -> MessageField(40))
  val myMsg2 = Message(40,50) + (60 -> MessageField("70"))
  "A connection protocol factory" should {
    val protocol = ConnectionProtocolFactory.instantiate()
    "serialize push message properly" in {
      val cmd = ConnectionWorker.Push(None, myMsg1 :: myMsg2 :: Nil)
      val msg = protocol.write(cmd)
      msg must beSome
      log.debug("msg = " + msg)
      val readResult = protocol.read[ConnectionWorker.Push](msg.get)
      readResult must beSome
      readResult.get must_== cmd
    }
  }
}