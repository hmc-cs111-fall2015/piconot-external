package piconot.parser

import scala.util.parsing.combinator._
import picolib.semantics._
/**
 * -----------
 * Grammar
 * -----------
 * 
 *          r Rule ::= st sr* -> c (st)
 *          st State ::= Int
 *          c Command ::= N | E | S | W | X
 *          sr Surroundings ::== No (sr) | Nx (sr) | So (sr) ...
 *  
 */
object PicoParser extends JavaTokenParsers {
  

    // parsing interface
    def apply(s: String): ParseResult[List[Rule]] = parseAll(rule.*, s)

    // rules
    def rule: Parser[Rule] = startState~surroundings~"->"~command~(endState.?)~";" ^^ {
      case startState~surroundings~"->"~command~endState~";" ⇒  Rule( 
                                                          startState, 
                                                          and(surroundings, blank), 
                                                          command, 
                                                          endState getOrElse startState
                                                        )
    }
    def and(surroundingsList: List[Surroundings],toReturn: Surroundings): Surroundings = (surroundingsList,toReturn) match{
      case (Nil,toReturn) => toReturn
      case (No::rest,Surroundings(_,e,w,s)) => and(rest,Surroundings(Open,e,w,s))
      case (Nx::rest,Surroundings(_,e,w,s)) => and(rest,Surroundings(Blocked,e,w,s))
      case (So::rest,Surroundings(n,e,w,_)) => and(rest,Surroundings(n,e,w,Open))
      case (Sx::rest,Surroundings(n,e,w,_)) => and(rest,Surroundings(n,e,w,Blocked))
      case (Eo::rest,Surroundings(n,_,w,s)) => and(rest,Surroundings(n,Open,w,s))
      case (Ex::rest,Surroundings(n,_,w,s)) => and(rest,Surroundings(n,Blocked,w,s))
      case (Wo::rest,Surroundings(n,e,_,s)) => and(rest,Surroundings(n,e,Open,s))
      case (Wx::rest,Surroundings(n,e,_,s)) => and(rest,Surroundings(n,e,Blocked,s))
      case _ => throw new IllegalArgumentException("Bad argument, man")   
    }
    //startState
    def startState = state
    //endState
    def endState = state
    // state
    def state: Parser[State] = wholeNumber ^^ {s ⇒ State(s)}
    
    // surroundings
    def surrounding: Parser[Surroundings] =
      (   "No" ^^ {case _ => No}
        | "Nx" ^^ {case _ => Nx}
        | "Eo" ^^ {case _ => Eo}
        | "Ex" ^^ {case _ => Ex}
        | "Wo" ^^ {case _ => Wo}
        | "Wx" ^^ {case _ => Wx}
        | "So" ^^ {case _ => So}
        | "Sx" ^^ {case _ => Sx} )
        
    def surroundings: Parser[List[Surroundings]] = surrounding.*
            
    // commands
    def command: Parser[MoveDirection] =
      (   "N" ^^ {case _ => North}
        | "E" ^^ {case _ => East}
        | "W" ^^ {case _ => West}
        | "S" ^^ {case _ => South}
        | "X" ^^ {case _ => StayHere})
      
    val blank = Surroundings(Anything,Anything,Anything,Anything)
    val No = Surroundings(Open,Anything,Anything,Anything)
    val Nx = Surroundings(Blocked,Anything,Anything,Anything)
    val So = Surroundings(Anything,Anything,Anything,Open)
    val Sx = Surroundings(Anything,Anything,Anything,Blocked)
    val Eo = Surroundings(Anything,Open,Anything,Anything)
    val Ex = Surroundings(Anything,Blocked,Anything,Anything)
    val Wo = Surroundings(Anything,Anything,Open,Anything)
    val Wx = Surroundings(Anything,Anything,Blocked,Anything)
      
 }
