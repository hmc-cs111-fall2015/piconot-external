package language.parser

import scala.language.implicitConversions
import org.scalacheck.Prop.forAll
import piconot.parser.PicoParser
import org.scalacheck.Properties
import picolib.semantics._

object PicoParseSpec extends Properties("Parser") {

    // some syntactic sugar for expressing parser tests
    implicit class ParseResultChecker(input: String) {
      def ~>(output: List[Rule]) = {
        val result = PicoParser(input)
        result.successful && result.get == output
      }
    }
    
    property("change of state, one specification") = 
      "0 No -> N 1;" ~> 
    List(Rule(
        State("0"),
        Surroundings(Open,Anything,Anything,Anything),
        North,
        State("1")))
    
    property("no change of state, one specification") = 
      "0 No -> N;" ~> 
    List(Rule(
        State("0"),
        Surroundings(Open,Anything,Anything,Anything),
        North,
        State("0")))
        
    property("change of state, four specification") = 
      "0 No Ex Wo Sx-> W 1;" ~> 
    List(Rule(
        State("0"),
        Surroundings(Open,Blocked,Open,Blocked),
        West,
        State("1")))
    property("multiple rules") = 
      "0 No -> N 1; 1 Nx Wo -> E 2;" ~> 
    List(Rule(
        State("0"),
        Surroundings(Open,Anything,Anything,Anything),
        North,
        State("1")),
        Rule(
            State("1"),
            Surroundings(Blocked,Anything,Open,Anything),
            East,
            State("2")
            )
        )
    property("multiple rules, no state changes") = 
      "0 No -> N; 0 Nx Wo -> E;" ~> 
    List(Rule(
        State("0"),
        Surroundings(Open,Anything,Anything,Anything),
        North,
        State("0")),
        Rule(
            State("0"),
            Surroundings(Blocked,Anything,Open,Anything),
            East,
            State("0")
            )
        )
    
}