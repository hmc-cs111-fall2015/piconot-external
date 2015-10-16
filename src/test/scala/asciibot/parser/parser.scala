package asciibot.parser

//import scala.language.implicitConversions
import org.scalatest.Matchers
import org.scalatest.FunSuite

import AsciibotParser._

import picolib.semantics._

class AsciibotParserTest extends FunSuite with Matchers {

  test("Input: All Blocked") {
    val inStateStrs = List(" # ","#1#"," # ")
    parseInState(inStateStrs, 0, 3) should be
    (Surroundings(Blocked,Blocked,Blocked,Blocked),State("1"))
  }

  test("Input: Mixed Surroundings") {
    val inStateStrs = List(" _ ","*a_"," # ")
    parseInState(inStateStrs, 0, 3) should be
    (Surroundings(Open,Open,Anything,Blocked),State("a"))
  }

  test("Input: Long State") {
    val inStateStrs = List(" _   ","*bat_","  *  ")
    parseInState(inStateStrs, 0, 5) should be
    (Surroundings(Open,Open,Anything,Anything),State("bat"))
  }

  test("Input: Missing Top Surrounding") {
    val inStateStrs = List("     ","*bat_","  *  ")
    an [CharBlockError] should be thrownBy
    parseInState(inStateStrs, 0, 5)
  }

  test("Input: Missing Middle Surrounding") {
    val inStateStrs = List("  #  "," bat_","  *  ")
    an [CharBlockError] should be thrownBy
    parseInState(inStateStrs, 0, 5)
  }
}

