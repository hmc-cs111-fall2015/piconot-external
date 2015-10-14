package picoPrecious

import picolib.semantics._
import picoPrecious.ir._

/**
 * @author Zoab
 */
package object semantics {
    class TooManySurroundingsException(message: String) extends Exception
  
    def convertToRules(rules: List[RuleBuilder]): List[Rule] = 
     rules.map { x => convertRuleBuilder(x) }
    
    def convertRuleBuilder(rule: RuleBuilder): Rule = {
      var northContents: RelativeDescription = Anything
      var eastContents: RelativeDescription = Anything
      var westContents: RelativeDescription = Anything
      var southContents: RelativeDescription = Anything
      def buildDirection(dir: DirectionAndContents): Unit = {
        dir match {
          case a if a.dir == North => northContents = a.contents
          case a if a.dir == East => eastContents = a.contents
          case a if a.dir == West => westContents = a.contents
          case a if a.dir == South => southContents = a.contents
        }
      }
      if (rule.surroundings.directions.size > 4) {
        throw new TooManySurroundingsException("You can't specify more than 4 surroundings states!")
      }
      rule.surroundings.directions.map(buildDirection)
      Rule(rule.initialState, Surroundings(northContents,eastContents,westContents,southContents),
          rule.direction, if (rule.finalState == null) rule.initialState else rule.finalState)
    }
}