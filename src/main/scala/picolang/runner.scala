package picolang

import java.io.File
import scalafx.application.JFXApp
import scalafx.application.Platform
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
        case parser.NoSuccess(msg, rest) =>
          val pos = rest.pos
          Console.err.println(pos.longString)
          Console.err.println("Bot file position " + pos.toString + ": " + msg)
          Platform.exit()
      }
    case _ =>
      Console.err.println("Please provide --maze=file and --bot=file parameters")
      Platform.exit()
  }
}