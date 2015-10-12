package asciibot.parser

//import scala.language.implicitConversions
import org.scalatest.Matchers
import org.scalatest.FunSuite

import AsciibotParser._

import picolib.semantics._

class AsciibotParserTest extends FunSuite with Matchers {

    test("All Blocked") {
        val inStateStrs = List(" # ","#1#"," # ")
        parseInState(inStateStrs, 0, 3) should be
        (Surroundings(Blocked,Blocked,Blocked,Blocked),State("1"))
    }

}

