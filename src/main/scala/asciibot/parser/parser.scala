package asciibot.parser

import java.io.BufferedReader

import scala.collection.mutable.MutableList
import picolib.maze.Maze
import picolib.semantics._

case class BlockError(m: String, sc: Int, ec: Int, line: Int) extends RuntimeException
case class LineError(m: String, line: Int) extends RuntimeException

object AsciibotParser {

  def parseLines(program: Iterator[String]) : List[Rule] = {
    val lines: MutableList[String]= new MutableList
    val rules: MutableList[Rule] = new MutableList
    program.zipWithIndex foreach { case (line, lineNum) => {
      val noComment = line.split("//")(0)
      if (noComment.trim.equals("")) {
        if (lines.length > 0)
          throw new LineError("Blank line in middle of rule", lineNum)
      } else {
        lines += noComment
        if (lines.length == 3) {
          try {
            rules ++= parseLine(lines)
          } catch {
            case BlockError(m, sc, ec, _) => throw new BlockError(m, sc, ec, lineNum)
          }
          lines.clear
        }
      }
    } }
    rules.toList
  }

  def parseLine(lns: Seq[String]) : List[Rule] = {
    assert(lns.length == 3)
    val maxLen = (lns map (l => l.length)).max
    // Make sure each string is the same length
    val nlns = lns map (_.padTo(maxLen, ' '))

    val rules: MutableList[Rule] = new MutableList

    var ruleStart = 0

    // iterate over columns
    var i = 0
    for (i <- 0 until maxLen) {
      // Number of bars in a colum
      val numBars = (nlns map (l => l(i)) filter (_=='|')).length
      if (!(numBars == 0 || numBars == 3)) {
        //throw new RuleSeparatorError(i)
        throw new BlockError("Vertical bar unexpected", i,i+1, 0)
      }
      if (numBars == 3) {
        rules += parseRule(nlns, ruleStart, i)
        ruleStart = i + 1
      }
    }
    rules += parseRule(nlns, ruleStart, maxLen)

    rules.toList
  }

  def parseRule(lines: Seq[String], start: Int, end: Int) : Rule = {
    def throwBE(m : String) = throw new BlockError(m, start, end, 0)
    val arrowStart = lines(1).indexOfSlice("->", start)
    if (arrowStart < start || arrowStart >= end) {
      throwBE("No arrow found")
    }
    val (inStateStart, inStateEnd) = trimBlock(lines, start, arrowStart)
    val (outStateStart, outStateEnd) = trimBlock(lines, arrowStart + 2, end) 
    val (surrs, inState) = parseInState(lines, inStateStart, inStateEnd)
    val (md, outState) = parseOutState(lines, outStateStart, outStateEnd, surrs)
    Rule(inState, surrs, md, outState)
  }

  def trimBlock(lines: Seq[String], start: Int, end: Int) : (Int, Int) = {
    var s = start
    var e = end
    while (lines(0)(s) == ' ' && lines(1)(s) == ' ' && lines(2)(s) == ' ') {
      s += 1;
    }
    while (lines(0)(e - 1) == ' ' && lines(1)(e - 1) == ' ' && lines(1)(e - 1) == ' ') {
      e -= 1;
    }
    (s, e)
  }

  def parseInState(a: Seq[String], start: Int, end: Int) : (Surroundings, State) = {
    def throwBE(m : String) = throw new BlockError(m, start, end, 0)
    assert(a.length == 3)
    val state_str = a(1).slice(start+1,end-1)
    // Check to make sure there are no illegal characters in the state name
    if ("#*_|-/> " exists (state_str contains _)) {
      //throw new StateNameError(state_str)
      throw new BlockError(s"Bad state name `$state_str'", start, end, 0)
    }

    if (   a(0)(start) != ' '
        || a(0)(end-1) != ' '
        || a(2)(start) != ' '
        || a(2)(end-1) != ' ') {
        throwBE("Corners of state box not empty")
    }

    // NEWS order, strings
    val str_surrs = List(
      ("Top", a(0).slice(start,end) filter (_ != ' ')), // TODO: change to partition or something
      ("Right", a(1)(end-1).toString),
      ("Left", a(1)(start).toString),
      ("Bottom", a(2).slice(start,end)  filter (_ != ' '))
    )

    val surrs = str_surrs map (_ match {
        case (_, "#") => Blocked
        case (_, "_") => Open
        case (_, "*") => Anything
        case (dir, s) => throw new BlockError(s"Invalid Surrounding `$s' for $dir side", start, end, 0)
      })

    (Surroundings(surrs(0), surrs(1), surrs(2), surrs(3)), State(state_str))
  }

  def parseOutState(a: Seq[String],
                    start: Int,
                    end: Int,
                    surrs: Surroundings) : (MoveDirection, State) = {
    def throwBE(m : String) = throw new BlockError(m, start, end, 0)
  
    //println(s"parseOutState: $a")
    assert(a.length == 3)

    val sym = "[#_*]"
    val id = "[^#_*/\\-> |]*"
    // >0 spaces, then either an #, o, or *, or a state name, then >0 spaces
    val topBotRegex = s"""^ +(${id}|${sym}) +$$""".r
    // id or sym, id or spaces, id or sym
    val midRegex = s"""^(${id}|${sym})(${id}| +)(${id}|${sym})$$""".r

    val tmo = topBotRegex findPrefixMatchOf a(0).slice(start,end)
    val mmo = midRegex findPrefixMatchOf a(1).slice(start,end)
    val bmo = topBotRegex findPrefixMatchOf a(2).slice(start,end)

    // Unwrap options and make sure the regex was matched
    val tm = tmo getOrElse throwBE("Top row malformed")
    val mm = mmo getOrElse throwBE("Middle row malformed")
    val bm = bmo getOrElse throwBE("Bottom row malformed")

    // Verify column alignment
    if (tm.start(1) < mm.start(2) || tm.end(1) > mm.end(2)) {
      throwBE("top and middle not aligned")
    }
    if (bm.start(1) < mm.start(2) || bm.end(1) > mm.end(2)) {
      throwBE("middle and bottom not aligned")
    }
    
    // Strings that are in the north, east, west, south, and center positions
    val matches = List(tm.group(1), mm.group(3), mm.group(1), bm.group(1), mm.group(2))

    println("matches are " +  matches)

    // Make sure only one state is specified
    val numOutputStates = matches count {s => !("#_* " contains s(0))}
    if ( 1 != numOutputStates ) {
      throwBE(s"$numOutputStates output states were provided")
    }

    // Make sure that output surroundings match input surroundings
    val Surroundings(inTop, inRight, inLeft, inBot) = surrs
    val inSurrList = List(inTop, inRight, inLeft, inBot)
    inSurrList zip matches.slice(0,4) foreach { _ match {
      case (Blocked, "#") => ()
      case (Open, "_") => ()
      case (Anything, "*") => ()
      case (_, s) if !("#_*" contains s(0)) => () // Surrounding in input, state in output
      case (i,o) => throwBE(s"Surrounding mismatch: ${surrToChar(i)} to $o")
      }
    }

    val moveDirIdx = matches indexWhere (s => !("#_*" contains s(0)))
    val moveDir = List(North, East, West, South, StayHere)(moveDirIdx)
    val nextState = State(matches(moveDirIdx))
    (moveDir, nextState)
  }

  def surrToChar(s: RelativeDescription) : String = s match {
    case Blocked => "#"
    case Open => "_"
    case Anything => "*"
  }
}
