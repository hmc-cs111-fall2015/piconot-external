package picoPrecious

import scalafx.application.JFXApp
import picolib.maze.Maze
import java.io.File
import picoPrecious.parser._
import picoPrecious.semantics.convertToRules
import picoPrecious.semantics.ErrorChecker._
import picolib.semantics._

/**
 * @author Zoab
 */
object pico extends JFXApp {
  
    val args = parameters.raw
    
    if (args.length != 2) {
      println(s"Not the right number of arguments: Got ${args.length}, expected 2")
      sys.exit(1)
    }
    
    val maze = Maze(args(0))
    val program = PicoParser(io.Source.fromFile(args(1)).mkString)
    
    program match {
      case e: PicoParser.NoSuccess => println(e) 
      
      case PicoParser.Success(ruleBuilders, _) => {
         error_check(ruleBuilders)
         object GollumBot extends Picobot(maze, convertToRules(ruleBuilders))
         with TextDisplay with GUIDisplay

        stage = GollumBot.mainStage
        
        GollumBot.run()
      }
    }
}