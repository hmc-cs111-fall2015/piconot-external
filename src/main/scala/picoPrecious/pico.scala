package picoPrecious

import scalafx.application.JFXApp
import picolib.maze.Maze
import java.io.File
import picoPrecious.parser._
import picoPrecious.semantics.eval
import picolib.semantics._

/**
 * @author Zoab
 */
object pico extends JFXApp {
  
    val args = parameters.raw
    
    if (args.length != 2) {
      println("Not the right number of arguments!")
      sys.exit(1)
    }
    
    val maze = Maze(args(0))
    val program = PicoParser(io.Source.fromFile(args(1)).mkString)
    
    program match {
      case e: PicoParser.NoSuccess => println(e) 
      
      case PicoParser.Success(t, _) => {
         object GollumBot extends Picobot(maze, eval(t))
         with TextDisplay with GUIDisplay

        stage = GollumBot.mainStage
        
        GollumBot.run()
      }
    }
}