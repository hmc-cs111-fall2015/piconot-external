package piconot

import scalafx.application.JFXApp.PrimaryStage
import picolib.semantics._
import picolib.maze.Maze
import parser.PicoParser
import java.io.File
import scalafx.application.JFXApp
import picolib.maze.Maze
import picolib.semantics._
import java.io.File

object Piconot extends App {
  val maze = Maze("resources" + File.separator + "empty.txt")
  PicoParser(io.Source.fromFile("empty_code.txt").mkString) match {
    case PicoParser.Success(rules, _) ⇒ {    
                  object EmptyBot extends Picobot(maze, rules)
                  with TextDisplay with GUIDisplay
                  EmptyBot.run()
                  
     }
    case e: PicoParser.NoSuccess  ⇒ println(e)
  }
}
