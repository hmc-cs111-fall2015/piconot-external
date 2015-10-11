package asciibot

import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

import parser._

import scala.collection.mutable.MutableList
import picolib.maze.Maze
import picolib.semantics._

object AsciiBot extends App {
  if (args.length != 2) {
    println("Needs 2 arguments: maze file and program file")
  }

  val rules : List[Rule] = try {
    AsciibotParser.parseFile(args(1))
  } catch {
    case BlockError(m, sc, ec, line) =>
      println(s"Error: lines ${line-1} - ${line+1} chars ${sc+1} - ${ec}: $m")
      System.exit(1)
      List()
    case LineError(m,line) =>
      println(s"Error: line ${line}: $m")
      System.exit(1)
      List()
  }
  println(rules)


  object ourBot extends Picobot(Maze(args(0)), rules)
    with TextDisplay //with semantics.GUIDisplay

  ourBot.run()

  //stage = EmptyBot.mainStage
}
