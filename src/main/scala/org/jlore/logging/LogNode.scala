package org.jlore.logging

class LogNode(val name:String, private var _effectiveLevel:LogLevel = LogLevel.TRACE) {
  private var _level: Option[LogLevel] = None
  private var nodes: Map[String,LogNode] = Map.empty
  def level = _effectiveLevel
  
  def subNode(path: Seq[String]): LogNode = {
    if (path.isEmpty) this else {
      nodes.getOrElse (path.head, {
        val node = new LogNode(name + "." + path.head, _effectiveLevel)
        nodes += (path.head -> node)
        node
      }).subNode(path.tail)
    }
  }
  
  def setLevel (l: LogLevel) {
    _level = Some(l)
    _effectiveLevel = l
    setEffective(l)
  }
  
  private def setEffective (l: LogLevel) {
    if (_level.isEmpty) {
      _effectiveLevel = l
      nodes.values.foreach {
        _.setEffective(l)
      }      
    }
  }
  
  def |- (l: LogLevel) { setLevel(l); }
}
