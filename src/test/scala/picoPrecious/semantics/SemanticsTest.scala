package picoPrecious.semantics

import scala.language.implicitConversions
import org.scalacheck._
import org.scalacheck.Prop.{forAll,BooleanOperators,throws}
import Gen._
import Arbitrary.arbitrary
import java.lang.{ IllegalArgumentException => IAE }
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.scalatest.Assertions._
import picoPrecious.ir._
import picoPrecious.ir.sugar._
import picoPrecious.semantics._
import picoPrecious.semantics.ErrorChecker._
import picolib.semantics._

object PicoInterpreterSpec extends Properties("Interpreter") {
  
    // some syntactic sugar for expressing interpreter tests
    implicit class StateChecker(input: State) {
      def ~>(output: String) = {
        val result = input.toString()
        result == output
      }
    }
    
    implicit class DACChecker(input: DirectionAndContents) {
      def ~>(dirOut: MoveDirection, cOut: RelativeDescription) = {
        input.dir == dirOut && input.contents == cOut
      }
    }
    
    implicit val arbDirection: Arbitrary[MoveDirection] = Arbitrary(oneOf(List(North, South, East, West)))
    implicit val arbContents: Arbitrary[RelativeDescription] = Arbitrary(oneOf(List(Open, Anything, Blocked)))

    // Generators for ASTs
    val state = for {
      n ← arbitrary[Int]
    } yield State(n.toString())
    
    property("state") = forAll { (n: Int) => State(n.toString()) ~> n.toString() }
    
    val directionAndContents = for {
      d <- arbitrary[MoveDirection]
      c <- arbitrary[RelativeDescription]
    } yield DirectionAndContents(c, d)
    
    property("directionAndContents") = forAll { (d: MoveDirection, c: RelativeDescription) => DirectionAndContents(c, d).~>(d, c) }
}

@RunWith(classOf[JUnitRunner])
class SemanticsTest extends Specification {
  val north = DirectionAndContents(Blocked, North)
  val north2 = DirectionAndContents(Anything, North)
  val east = DirectionAndContents(Blocked, East)
  val east2 = DirectionAndContents(Anything, East)
  val west = DirectionAndContents(Anything, West)
  val south = DirectionAndContents(Anything, South)
  
  val state1 = State("1")
  
  "SurroundingSetters" should {
    "have no more than four objects" in {      
      val badSurroundings = SurroundingSetter(List(north, east, west, south, north2))
      val rb1 = new RuleBuilder(state1, badSurroundings, North, state1)
      intercept[TooManySurroundingsException] {
        checkOneRule(rb1)
      }
      
      true
    }    
    
    "have at least one object" in {   
      val badSurroundings = SurroundingSetter(List())
      val rb = new RuleBuilder(state1, badSurroundings, North, state1)
      intercept[NoSurroundingsException] {
        checkOneRule(rb)
      }
      true
    }
    
    "have at least one non-wildcard" in {
      val badSurroundings = SurroundingSetter(List(north2, east2, west, south))
      val rb = new RuleBuilder(state1, badSurroundings, North, state1)
      intercept[AllWildcardsException] {
        checkOneRule(rb)
      }
      true
    }
    
    "have no duplicates" in {
      val badSurroundings1 = SurroundingSetter(List(north, north))
      val badSurroundings2 = SurroundingSetter(List(north, north2))
      val rb1 = new RuleBuilder(state1, badSurroundings1, North, state1)
      intercept[DuplicateSurroundingsException] {
        checkOneRule(rb1)
      }
      
      val rb2 = new RuleBuilder(state1, badSurroundings2, North, state1)
      intercept[DuplicateSurroundingsException] {
        checkOneRule(rb2)
      }
      true
    }
    
    "not have a StayHere" in {
      val badSurroundings = SurroundingSetter(List(new DirectionAndContents(Blocked, StayHere)))
      intercept[IAE] {
        checkOneRule(new RuleBuilder(state1, badSurroundings, North, state1))
      }
      true
    }
    
  }
  
  "RuleBuilder" should {
    "not make a rule that moves in a blocked direction" in {
       val surroundings = SurroundingSetter(List(east))
       val rb = new RuleBuilder(state1, surroundings, East, state1)
       intercept[InvalidMoveDirectionException] {
         checkOneRule(rb)
       }
       
       true
    }
  }

}