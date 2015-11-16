package simpleEMS.ir

/**
 * @author mjeong
 */

object Occupant {
  var name = "\n"
  
  def setName(newName: String): Unit = {
    name = newName
  }
  
  def getName(): String = {
    name
  }
}