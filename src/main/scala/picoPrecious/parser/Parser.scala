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
    ( startState~"and"~surroundings~"then"~direction~"and"~finalState~"." 
        ^^ {case a~"and"~b~"then"~c~"and"~d~"." => 
          RuleBuilder(a, b, c, d)}
        )
   
   lazy val startState: PackratParser[State] = 
     ( "If you are holding weapon "~weapon ^^ {case "If you are holding weapon "~w => w}
         )
         
   def weapon: Parser[State] = wholeNumber ^^ {x => State(x.toString())}
  
  lazy val direction: PackratParser[MoveDirection] = 
    ( "go towards the Shire" ^^ {case "go towards the Shire" => North} |
      "go towards the Lonely Mountain" ^^ {case "go towards the Lonely Mountain" => East} |
      "go towards the Undying Lands" ^^ {case "go towards the Undying Lands" => West} |
      "go towards Mordor" ^^ {case "go towards Mordor" => South})
      
  lazy val finalState: PackratParser[State] = 
     ( "ready weapon "~weapon ^^ {case "ready weapon "~w => w }
         )
         
  lazy val surroundings: PackratParser[SurroundingSetter] = 
    (surrounding~","~surrounding~","~surrounding~","~surrounding~"," ^^ {case s1~","~s2~","~s3~","~s4~"," => 
      SurroundingSetter(s1,s2,s3,s4)} |
     surrounding~","~surrounding~","~surrounding~"," ^^ {case s1~","~s2~","~s3~"," => 
      SurroundingSetter(s1,s2,s3,null)} |
     surrounding~","~surrounding~"," ^^ {case s1~","~s2~"," => 
      SurroundingSetter(s1,s2,null,null)} |
     surrounding~"," ^^ {case s1~"," => 
      SurroundingSetter(s1,null,null,null)})
          
  lazy val surrounding: PackratParser[DirectionAndContents] = 
    ( contents~"towards"~location ^^ {case c~"towards"~l => c towards l }
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