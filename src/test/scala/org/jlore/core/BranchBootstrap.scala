package org.jlore.core

import org.jlore.model.i18n.Locale
import org.jlore.model.i18n.Plural
import org.jlore.model.i18n.Name
import org.jlore.model.Value

class BranchBootstrap {
  private var _b = new Branch
  def b = _b
  def b_= (value: Branch):Unit = _b = value
  
  val en = VersionedObject[Locale]() 
  _b += new Locale.Create(ID(), en)
  _b += new Locale.SetIdentifier(ID(), en, Some(Value("en")))
  val en_single = VersionedObject[Plural]()
  _b += new Plural.Create(ID(), en_single)
  _b += new Plural.SetLocalName(ID(), en_single, Some(Value("single")))
  val en_plural = VersionedObject[Plural]()
  _b += new Plural.Create(ID(), en_plural)
  _b += new Plural.SetLocalName(ID(), en_plural, Some(Value("plural")))
  
  def single(s: String) = {
    val name = VersionedObject[Name]()
    _b += new Name.Create(ID(), name, en, en_single)
    _b += new Name.SetLocalName(ID(), name, Some(Value(s)))
    name
  }
  
  implicit def mkName (s_p: (String, String)) = {
    val name_single = VersionedObject[Name]()
    _b += new Name.Create(ID(), name_single, en, en_single)
    _b += new Name.SetLocalName(ID(), name_single, Some(Value(s_p._1)))
    val name_plural = VersionedObject[Name]()
    _b += new Name.Create(ID(), name_plural, en, en_plural)
    _b += new Name.SetLocalName(ID(), name_plural, Some(Value(s_p._2)))
    name_single :: name_plural :: Nil
  }
}