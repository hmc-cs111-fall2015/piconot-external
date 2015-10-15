package piconot.example

import scala.tools.nsc.EvalLoop
//import piconot.semantics.eval
import piconot.semantics.eval
import piconot.interpreter
import piconot.parser._
import piconot.ir._
import java.io.File
//import scalafx.application.JFXApp
/**
 * @author mvalentine
 */

object MazeExample extends interpreter {
//  override def prompt = "> "
//
//  loop { line ⇒
//    PiconotParser(line) match {
//      case PiconotParser.Success(t, _) ⇒ println(eval(t))
//      case e: PiconotParser.NoSuccess ⇒ println(e)
//    }
//  }
    val lines = scala.io.Source.fromFile("resources" + File.separator + s"maze2.txt").mkString
    val firstLineEnd = lines.indexOf("\n")
    println(firstLineEnd, firstLineEnd)
    val mazeName = lines.slice(0, firstLineEnd).trim
    val newLine = lines.slice(firstLineEnd, lines.length())
    PiconotParser(newLine) match {
      case PiconotParser.Success(t, _) ⇒ {
        println(t)
        eval(t)(mazeName)
      }
      case e: PiconotParser.NoSuccess  ⇒ println(e)
    }
}