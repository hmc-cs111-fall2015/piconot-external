package picolang.parsing

import picolang.ir._
import scala.util.parsing.combinator._


/**
 * @author apinson dhouck
 */
object parser extends JavaTokenParsers with PackratParsers{
  
  // parsing interface
  def apply(s: String): ParseResult[AST] = parseAll(state.+, s)
  
  /**
   * Each state is defined by "state STATENAME", followed by one or more rules
   */
  lazy val state: PackratParser[State] = log(
    rword("state")~>name~rule.+ ^^ { case n~rs => new State(n, rs) }
    )("state")
  
    /**
     * Each rule is defined by one or more surroundings, or "Any",
     * followed by "->" and then instructions
     */
  lazy val rule: PackratParser[Rule] = log(
    
    surroundings~"->"~rep1sep(action, ",".?)~(",".? ~>transition).? ^^ {
      case surr~"->"~actions~transition => new Rule(surr, actions, transition)
      }
    | surroundings~"->"~transition ^^ {case surr~"->"~transition =>
                                        new Rule(surr, List(), Some(transition))}
  )("rule")
  
  
  lazy val action: PackratParser[Action] = log(
      rword("go")~>direction ^^ {Go(_)}
      | rword("turn")~>direction ^^ {Turn(_)}
    )("action")
  
  lazy val transition: PackratParser[Name] = log(
    rword("transition")~>name  
    )("transition")
  
  lazy val direction: PackratParser[Direction] = log(
    rword("North") ^^^ North
    | rword("East") ^^^ East
    | rword("West") ^^^ West
    | rword("South") ^^^ South
    | rword("Forward") ^^^ Forward
    | rword("Back") ^^^ Back
    | rword("Left") ^^^ Left
    | rword("Right") ^^^ Right
    )("direction")
  
    /**
   * If there are surroundings to parse, create a map
   * that our AST can treat as its Rule's surroundings
   */
  lazy val surroundings: PackratParser[Map[Direction, Boolean]] = log( // TODO: error handling for consistency (no Nopen, Nclosed)
      rep1sep(surrounding, ",".?) ^^ {_.toMap}
      | "Any" ^^^ Map[Direction, Boolean](North -> true, 
                                          East -> true,
                                          West -> true,
                                          South -> true)
      )("surroundings")
    

  /**
   *  Each surroundings is a letter representing a direction
   *  followed by open or closed
   */
  lazy val surrounding: PackratParser[(Direction, Boolean)] = log(
    dirChar~"open" ^^ { case dir~"open" => (dir, true) }
    | dirChar~"closed" ^^ { case dir~"closed" => (dir, false) }
    )("surrounding")
    
  lazy val dirChar: PackratParser[Direction] = log(
    "N" ^^^ North
    | "E" ^^^ East
    | "W" ^^^ West
    | "S" ^^^ South
    | "F" ^^^ Forward
    | "B" ^^^ Back
    | "L" ^^^ Left
    | "R" ^^^ Right
    )("dirChar")
  
  def rword(word: String): PackratParser[String] = {
    ident filter {_ == word}
  }
  def name: Parser[Name] = ident
}
