package org.jlore

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.SpecificationWithJUnit
import org.jlore.logging.Log

@RunWith(classOf[JUnitRunner])
abstract class Specification extends SpecificationWithJUnit with Log {

}