package picolang

import java.io.File
import scalafx.application.JFXApp
import scala.util.parsing.combinator._

import picolang.semantics._
import picolang.parsing.parser
import picolib.semantics._

import picolib.maze.Maze

/**
 * @author dhouck
 */
object runner extends JFXApp {
  val mazeOpt = parameters.named get "maze"
  val botOpt = parameters.named get "bot"
  (mazeOpt, botOpt) match {
    case (Some(mazeFile), Some(botFile)) =>
      val maze = Maze(mazeFile)
      val botCode = io.Source.fromFile(botFile).mkString
      parser(botCode) match {
        case parser.Success(bot, _) => 
          val rules = toRules(bot)
          object Bot extends Picobot(maze, rules) with TextDisplay with GUIDisplay
          Bot.run()
          stage = Bot.mainStage
        case err: parser.NoSuccess =>
          Console.err.println(err.msg)
          System.exit(1)
      }
    case _ =>
      Console.err.println("Please provide --maze=file and --bot=file parameters")
      System.exit(2)
  }
}