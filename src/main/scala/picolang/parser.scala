package picolang.parsing

import picolang.ir._
import scala.util.parsing.combinator._

/**
 * CHECK: "N open"
 * do 2 TODOs
 * make "Any" work
 * 
 * add error handling, warnings, etc.
 */


/**
 * @author apinson dhouck
 */
object parser extends JavaTokenParsers with PackratParsers{
  
  // parsing interface
  def apply(s: String): ParseResult[AST] = parseAll(state.+, s)
  
  /**
   * Each state is defined by "state STATENAME", followed by one or more rules
   */
  lazy val state: PackratParser[State] = (
    "state"~>name~rule.+ ^^ { case n~rs => new State(n, rs) }
    )
  
    /**
     * Each rule is defined by one or more surroundings, or "Any",
     * followed by "->" and then instructions
     */
  lazy val rule: PackratParser[Rule] = (
    surrMap~"->"~action.* ~transition.? ^^ {    //TODO: require action or transition
      case surr~"->"~actions~transition => new Rule(surr, actions, transition)
      }
//    | surrMap~"->"~>action.+ ^^ {case }
//    | "Any" ^^^ 
  )
  
  lazy val action: PackratParser[Action] = (
      "go"~>direction ^^ {Go(_)}
      | "turn"~>direction ^^ {Turn(_)}
    )
  
  lazy val transition: PackratParser[Name] = (
    "transition"~>name  
    )
  
  lazy val direction: PackratParser[Direction] = (
    "North" ^^^ North
    | "East" ^^^ East
    | "West" ^^^ West
    | "South" ^^^ South
    | "Forward" ^^^ Forward
    | "Back" ^^^ Back
    | "Left" ^^^ Left
    | "Right" ^^^ Right
    )
  
  /**
   * If there are surroundings to parse, recursively create a map
   * that our AST can treat as its Rule's surroundings
   */
  lazy val surrMap: PackratParser[Map[Direction, Boolean]] = (
     surroundings~(","~>surrMap).? ^^ {
       case pair~opMap => (opMap getOrElse Map()) + pair} //TODO: error if pair already exists
     )
  
  /**
   *  Each surroundings is a letter representing a direction
   *  followed by open or closed
   */
  lazy val surroundings: PackratParser[(Direction, Boolean)] = (
    dirChar~"open" ^^ { case dir~"open" => (dir, true) }
    | dirChar~"closed" ^^ { case dir~"closed" => (dir, false) }
    )
    
  lazy val dirChar: PackratParser[Direction] = (
    "N" ^^^ North
    | "E" ^^^ East
    | "W" ^^^ West
    | "S" ^^^ South
    | "F" ^^^ Forward
    | "B" ^^^ Back
    | "L" ^^^ Left
    | "R" ^^^ Right
    )
  
  def name: Parser[Name] = ident
  
}