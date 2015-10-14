package picolang.parsing

import picolang.ir._
import scala.util.parsing.combinator._

/**
 * @author apinson dhouck
 */
object parser extends JavaTokenParsers with PackratParsers{
  
  // parsing interface
  def apply(s: String): ParseResult[AST] = parseAll(state.+, s)
  
  lazy val state: PackratParser[State] = (
    "state"~>name~rule.+ ^^ { case n~rs => new State(n, rs) }
    )
  
  lazy val rule: PackratParser[Rule] = (
    
  )
    
  def name: Parser[Name] = ident
  
}