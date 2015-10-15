package picolang.parsing

import picolang.ir._
import scala.util.parsing.combinator._


/**
 * @author apinson dhouck
 * 
 * Note: This provides better error messages than default, but we couldn't
 * figure out how to precisely control which of several applicable error
 * messages was chosen. We also do not handle the various types of semantic
 * errors possible, only syntactic ones.
 */
object parser extends JavaTokenParsers with PackratParsers{
  
  // parsing interface
  def apply(s: String): ParseResult[AST] = parseAll(state.+, s)
  
  /**
   * Each state is defined by "state STATENAME", followed by one or more rules
   */
  lazy val state: PackratParser[State] = (
    rword("state")~>name~rule.+ ^^ { case n~rs => new State(n, rs) }
    | rword("state")~name~>failure("Expected list of rules")
    )
  
    /**
     * Each rule is defined by one or more surroundings, or "Any",
     * followed by "->" and then instructions
     */
  lazy val rule: PackratParser[Rule] = (
    
    surroundings~"->"~rep1sep(action, ",".?)~(",".? ~>transition).? ^^ {
      case surr~"->"~actions~transition => new Rule(surr, actions, transition)
      }
    | surroundings~"->"~transition ^^ {case surr~"->"~transition =>
                                        new Rule(surr, List(), Some(transition))}
    | failure("Expected list of instructions")
  )
  
  
  lazy val action: PackratParser[Action] = (
      rword("go")~>direction ^^ {Go(_)}
      | rword("turn")~>direction ^^ {Turn(_)}
      | failure("Expected action (e.g., \"go North\")")
    )
  
  lazy val transition: PackratParser[Name] = (
    rword("transition")~>name 
    | failure ("Expected transition (e.g., \"transition Start\")")
    )
  
  lazy val direction: PackratParser[Direction] = (
    rword("North") ^^^ North
    | rword("East") ^^^ East
    | rword("West") ^^^ West
    | rword("South") ^^^ South
    | rword("Forward") ^^^ Forward
    | rword("Back") ^^^ Back
    | rword("Left") ^^^ Left
    | rword("Right") ^^^ Right
    ) withFailureMessage "Expected direction (e.g., \"North\")"
  
    /**
   * If there are surroundings to parse, create a map
   * that our AST can treat as its Rule's surroundings
   */
  lazy val surroundings: PackratParser[Map[Direction, Boolean]] =  (
      rep1sep(surrounding, ",".?) ^^ {_.toMap}
      | rword("Any") ^^^ Map[Direction, Boolean]()
      ) withFailureMessage "Expected list of surroundings specifications"
    

  /**
   *  Each surroundings is a letter representing a direction
   *  followed by open or closed
   */
  lazy val surrounding: PackratParser[(Direction, Boolean)] = (
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
  
  def rword(word: String): PackratParser[String] = {
    ident filter {_ == word} withFailureMessage "Expected reserved word <" + word + ">."
  }
  def name: Parser[Name] = ident
}
