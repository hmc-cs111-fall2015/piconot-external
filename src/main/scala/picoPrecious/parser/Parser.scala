package picoPrecious.parser

import scala.util.parsing.combinator._
import picoPrecious.ir._
import picoPrecious.ir.sugar._
import picoPrecious.semantics.ErrorChecker._
import picolib.semantics._

/**
 * @author Zoab
 */
    
abstract class GrammarException(msg: String) extends Exception
case class DefiniteArticleException(name: String, needsArticle: Boolean = true) extends 
  GrammarException(s"The $name ${if (needsArticle) "needs" else "doesn't need"} a definite article.")
case class ProperNounException(name: String) extends 
  GrammarException(s"$name is a proper noun and should be capitalized.")
  
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
      "then stay" ^^ {case "then stay" => StayHere} |
      
      // Error states
      "then go towards Shire" ^^ {case "then go towards Shire" => throw new DefiniteArticleException("Shire") } |
      "then go towards shire" ^^ {case "then go towards shire" => throw new DefiniteArticleException("Shire") } |
      "then go towards the shire" ^^ {case "then go towards the shire" => throw new ProperNounException("Shire") } |
      
      "then go towards Lonely Mountain" ^^ {case "then go towards Lonely Mountain" => throw new DefiniteArticleException("Lonely Mountain") } |
      "then go towards lonely Mountain" ^^ {case "then go towards lonely Mountain" => throw new DefiniteArticleException("Lonely Mountain") } |
      "then go towards Lonely mountain" ^^ {case "then go towards Lonely mountain" => throw new DefiniteArticleException("Lonely Mountain") } |
      "then go towards lonely mountain" ^^ {case "then go towards lonely mountain" => throw new DefiniteArticleException("Lonely Mountain") } |
      "then go towards the lonely Mountain" ^^ {case "then go towards the lonely Mountain" => throw new ProperNounException("Lonely Mountain") } |
      "then go towards the Lonely mountain" ^^ {case "then go towards the Lonely mountain" => throw new ProperNounException("Lonely Mountain") } |
      "then go towards the lonely mountain" ^^ {case "then go towards the lonely mountain" => throw new ProperNounException("Lonely Mountain") } |
      
      "then go towards Undying Lands" ^^ {case "then go towards Undying Lands" => throw new DefiniteArticleException("Undying Lands") } |
      "then go towards undying Lands" ^^ {case "then go towards undying Lands" => throw new DefiniteArticleException("Undying Lands") } |
      "then go towards Undying lands" ^^ {case "then go towards Undying lands" => throw new DefiniteArticleException("Undying Lands") } |
      "then go towards undying lands" ^^ {case "then go towards undying lands" => throw new DefiniteArticleException("Undying Lands") } |
      "then go towards the undying Lands" ^^ {case "then go towards the undying Lands" => throw new ProperNounException("Undying Lands") } |
      "then go towards the Undying lands" ^^ {case "then go towards the Undying lands" => throw new ProperNounException("Undying Lands") } |
      "then go towards the undying lands" ^^ {case "then go towards the undying lands" => throw new ProperNounException("Undying Lands") } |
      
      "then go towards the Mordor" ^^ {case "then go towards the Mordor" => throw new DefiniteArticleException("Mordor", false) } |
      "then go towards the mordor" ^^ {case "then go towards the mordor" => throw new DefiniteArticleException("Mordor", false) } |
      "then go towards mordor" ^^ {case "then go towards mordor" => throw new ProperNounException("Mordor") } 
    )
      
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
         "the shire" ^^ {case "the shire" => throw new ProperNounException("Shire")} |
         "shire" ^^ {case "shire" => throw new DefiniteArticleException("Shire")} |
         "Shire" ^^ {case "Shire" => throw new DefiniteArticleException("Shire")} |
         
         "the Lonely Mountain" ^^ {case "the Lonely Mountain" => East} |
         "the lonely Mountain" ^^ {case "the lonely Mountain" => throw new ProperNounException("Lonely Mountain")} |
         "the Lonely mountain" ^^ {case "the Lonely mountain" => throw new ProperNounException("Lonely Mountain")} |
         "the lonely mountain" ^^ {case "the lonely mountain" => throw new ProperNounException("Lonely Mountain")} |
         "lonely Mountain" ^^ {case "lonely Mountain" => throw new DefiniteArticleException("Lonely Mountain")} |
         "Lonely mountain" ^^ {case "Lonely mountain" => throw new DefiniteArticleException("Lonely Mountain")} |
         "lonely mountain" ^^ {case "lonely mountain" => throw new DefiniteArticleException("Lonely Mountain")} |
         "Lonely Mountain" ^^ {case "Lonely Mountain" => throw new DefiniteArticleException("Lonely Mountain")} |
         
         "the Undying Lands" ^^ {case "the Undying Lands" => West} |
         "the undying Lands" ^^ {case "the undying Lands" => throw new ProperNounException("Undying Lands")} |
         "the undying lands" ^^ {case "the undying lands" => throw new ProperNounException("Undying Lands")} |
         "the Undying lands" ^^ {case "the Undying lands" => throw new ProperNounException("Undying Lands")} |
         "undying Lands" ^^ {case "undying Lands" => throw new DefiniteArticleException("Undying Lands")} |
         "undying lands" ^^ {case "undying lands" => throw new DefiniteArticleException("Undying Lands")} |
         "Undying lands" ^^ {case "Undying lands" => throw new DefiniteArticleException("Undying Lands")} |
         "Undying Lands" ^^ {case "Undying Lands" => throw new DefiniteArticleException("Undying Lands")} |
         
         
                  
         "Mordor" ^^ {case "Mordor" => South} |
         "the Mordor" ^^ {case "the Mordor" => throw new DefiniteArticleException("Mordor", false)} |
         "the mordor" ^^ {case "the mordor" => throw new DefiniteArticleException("Mordor", false)} |
         "mordor" ^^ {case "mordor" => throw new ProperNounException("Mordor")})
}