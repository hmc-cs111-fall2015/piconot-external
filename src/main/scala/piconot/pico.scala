package piconot

import piconot.semantics.eval
import piconot.parser._
import piconot.ir._
/**
 * @author mvalentine
 */
class pico {
  
  def main( args:Array[String] ):Unit = {
    print("start")
    val lines = scala.io.Source.fromFile("maze2.txt").mkString
    val parse = PiconotParser(lines)
    PiconotParser(lines) match {
      case PiconotParser.Success(t, _) ⇒ eval(t)
      case e: PiconotParser.NoSuccess  ⇒ println(e)
    }
  }
}