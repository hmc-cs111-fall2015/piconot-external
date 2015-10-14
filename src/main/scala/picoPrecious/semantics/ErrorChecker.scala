package picoPrecious.semantics

import picoPrecious.ir._
import picolib.semantics._

/**
 * @author Zoab
 */
package object ErrorChecker {
  
    def error_check(rules : List[RuleBuilder]): Unit = {
      rules.foreach { x => checkOneRule(x) }
    }
    
    def checkOneRule(rule: RuleBuilder): Unit = {
      checkNumSurroundings(rule)
      checkNotAllWildcards(rule)
      checkNoDuplicateSurroundings(rule)
      checkDirToMoveNotBlocked(rule)
    }
    
    def checkNotAllWildcards(rule: RuleBuilder): Unit = {
      var foundNonWildcard: Boolean = false
      rule.surroundings.directions.foreach { x => foundNonWildcard = foundNonWildcard || x.contents != Anything }
      if (!foundNonWildcard) {
        throw new AllWildcardsException("Can't have a rule with no non-wildcards!")
      }
    }
    
    def checkNumSurroundings(rule: RuleBuilder): Unit = {
      if (rule.surroundings.directions.size > 4) {
        throw new TooManySurroundingsException("You can't specify more than 4 surroundings!")
      }
      if (rule.surroundings.directions.size == 0) {
        throw new NoSurroundingsException("You didn't specify any of the surroundings!")
      }
    }
    
    def checkNoDuplicateSurroundings(rule: RuleBuilder): Unit = {
      var northCount: Int = 0
      var eastCount: Int = 0
      var westCount: Int = 0
      var southCount: Int = 0
      rule.surroundings.directions.foreach { 
        x => x.dir match {
          case North => northCount += 1
          case East => eastCount += 1
          case West => westCount += 1
          case South => southCount += 1
          case _ => throw new IllegalArgumentException("Surrounding direction must be North, South, East, or West")
        }
      }
      if (northCount > 1 || eastCount > 1 || westCount > 1 || southCount > 1) {
        throw new DuplicateSurroundingsException("You specified a surrounding more than once!")
      }
    }
    
    def checkDirToMoveNotBlocked(rule: RuleBuilder): Unit = {
       val moveDir: MoveDirection = rule.direction
       if (moveDir != StayHere) {
         rule.surroundings.directions.foreach { x => if (x.dir == moveDir && x.contents == Blocked) 
           throw new InvalidMoveDirectionException("You're trying to move in a direction that's blocked!") }    
       }      
    }
}