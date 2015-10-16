package asciibot

import scala.io.Source

import parser._

import scala.collection.mutable.MutableList
import picolib.maze.Maze
import picolib.semantics._

object AsciiBot extends App {
  if (args.length != 2) {
    println("Needs 2 arguments: maze file and program file")
    System.exit(2)
  }

  val rules : List[Rule] = try {
    AsciibotParser.parseLines(Source.fromFile(args(1)).getLines)
  } catch {
    case BlockError(m, sc, ec, sl, el) =>
      println(s"Error: lines ${sl} - ${el} chars ${sc+1} - ${ec}: $m")
      List()
    case LineError(m,line) =>
      println(s"Error: line ${line+1}: $m")
      List()
    case FileError(m) =>
      println(s"Error: $m")
      List()
  }
  println(rules)

  if (!rules.isEmpty) {

    object ourBot extends Picobot(Maze(args(0)), rules)
      with TextDisplay //with semantics.GUIDisplay

    ourBot.run()
    //stage = EmptyBot.mainStage
  }
}
