package picoPrecious

import picolib.semantics._
import picoPrecious.ir._

/**
 * @author Zoab
 */
package object semantics {
    def eval(rules: List[RuleBuilder]): List[Rule] = 
     rules.map { x => evaluateRule(x) }
    
    def evaluateRule(rule: RuleBuilder): Rule = {
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
      rule.surroundings.directions.map(buildDirection)
      Rule(rule.initialState, Surroundings(northContents,eastContents,westContents,southContents),
          rule.direction, if (rule.finalState == null) rule.initialState else rule.finalState)
    }
}