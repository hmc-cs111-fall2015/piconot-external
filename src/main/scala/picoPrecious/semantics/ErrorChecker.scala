package picoPrecious.semantics

import picoPrecious.ir._
import picolib.semantics._

/**
 * @author Zoab
 */
package object ErrorChecker {
    class TooManySurroundingsException(message: String) extends Exception
    class NoSurroundingsException(message: String) extends Exception
    class AllWildcardsException(message: String) extends Exception
  
    def error_check(rules : List[RuleBuilder]): Unit = {
      rules.foreach { x => checkOneRule(x) }
    }
    
    def checkOneRule(rule: RuleBuilder): Unit = {
      checkNumSurroundings(rule)
    }
    
    def checkNotAllWildcards(rule: RuleBuilder): Unit = {
      var foundNonWildcard: Boolean = false
      rule.surroundings.directions.foreach { x => if (x.contents != Anything) foundNonWildcard = true }
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
}