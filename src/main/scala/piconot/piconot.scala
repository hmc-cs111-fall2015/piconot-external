package piconot

import picolib.semantics._
import picolib.maze.Maze
import parser.PicoParser
import java.io.File
import scalafx.application.JFXApp

object Piconot extends JFXApp {
  val maze = Maze("resources" + File.separator + "empty.txt")
  PicoParser(io.Source.fromFile("empty_code.txt").mkString) match {
    case PicoParser.Success(rules, _) ⇒ {    
                  object EmptyBot extends Picobot(maze, rules)
                  with TextDisplay with GUIDisplay
                  EmptyBot.run()
                  stage = EmptyBot.mainStage
     }
    case e: PicoParser.NoSuccess  ⇒ println(e)
  }
}
