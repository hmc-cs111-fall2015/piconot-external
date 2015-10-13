package picoPrecious.parser

import scala.util.parsing.combinator._
import picoPrecious.ir._
import picoPrecious.ir.sugar._
import picolib.semantics._

/**
 * @author Zoab
 */
object PicoParser extends JavaTokenParsers with PackratParsers {
  def apply(s: String): ParseResult[List[RuleBuilder]] = parseAll(program,s)
  
  def program: Parser[List[RuleBuilder]] = expr*
  
  lazy val expr: PackratParser[RuleBuilder] = 
    ( startState~surroundings~direction~finalState~"." 
        ^^ {case a~b~c~d~"." => 
          RuleBuilder(a, b, c, d)}
        )
   
   lazy val startState: PackratParser[State] = 
     ( "If you are holding weapon "~weapon ^^ {case "If you are holding weapon "~w => w}
         )
         
   def weapon: Parser[State] = wholeNumber ^^ {x => State(x.toString())}
  
  lazy val direction: PackratParser[MoveDirection] = 
    ( "then go towards the Shire" ^^ {case "then go towards the Shire" => North} |
      "then go towards the Lonely Mountain" ^^ {case "then go towards the Lonely Mountain" => East} |
      "then go towards the Undying Lands" ^^ {case "then go towards the Undying Lands" => West} |
      "then go towards Mordor" ^^ {case "then go towards Mordor" => South} |
      "then stay" ^^ {case "then stay" => StayHere})
      
  lazy val finalState: PackratParser[State] = 
     ( "and ready weapon "~weapon ^^ {case "and ready weapon "~w => w } |
       "" ^^ {case "" => null}
         )
         
  lazy val surroundings: PackratParser[SurroundingSetter] = 
    (surrounding ^^ {case s => SurroundingSetter(s)} 
    )
          
  lazy val surrounding: PackratParser[List[DirectionAndContents]] = 
    ( "and"~contents~"towards"~location~","~surrounding ^^ {case "and"~c~"towards"~l~","~s => (c towards l)::s } |
      "" ^^ {case "" => List() }
        )
   
   lazy val contents: Parser[RelativeDescription] = 
     ( "there is nothing" ^^ {case "there is nothing" => Open} | 
         "there are Orcs" ^^ {case "there are Orcs" => Blocked} | 
         "there are orcs" ^^ {case "there are orcs" => Blocked} | 
         "there is anything" ^^ {case "there is anything" => Anything})
     
   lazy val location: Parser[MoveDirection] =
     (   "the Shire" ^^ {case "the Shire" => North} |
         "the Lonely Mountain" ^^ {case "the Lonely Mountain" => East} |
         "the Undying Lands" ^^ {case "the Undying Lands" => West} |
         "Mordor" ^^ {case "Mordor" => South} )
}