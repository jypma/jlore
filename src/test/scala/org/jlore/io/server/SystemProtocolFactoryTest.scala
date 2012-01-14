package org.jlore.io.server
import org.jlore.io.msg.Message
import org.jlore.io.msg.MessageField
import org.jlore.model.Property
import org.jlore.core.ID
import org.jlore.core.VersionedObject
import org.jlore.model.Instance
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SystemProtocolFactoryTest  extends org.jlore.Specification {
  val myCmd1 = new Property.Create(ID(), VersionedObject[Property]())
  val myCmd2 = new Instance.Create(ID(), VersionedObject[Instance]())
  "A system protocol factory" should {
    val protocol = new SystemProtocolFactory().instantiate()
    "serialize push message properly" in {
      val cmd = System.Push(None, myCmd1 :: myCmd2 :: Nil)
      val msg = protocol.write(cmd)
      msg must beSome
      log.debug("msg = " + msg)
      val readResult = protocol.read[System.Push](msg.get)
      readResult must beSome
      readResult.get must_== cmd
    }
  }
}