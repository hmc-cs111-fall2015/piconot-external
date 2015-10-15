package picoPrecious.ir

import scala.language.implicitConversions
import picolib.semantics._

/**
 * @author Zoab
 */
object sugar {
  
  implicit class DACBuilder(val left: RelativeDescription) {
    def towards(right: MoveDirection) = DirectionAndContents(left, right);
  }
  
}