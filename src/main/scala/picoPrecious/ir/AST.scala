package picoPrecious.ir

import picolib.semantics._

/**
 * @author Zoab
 */

sealed abstract class AST
sealed abstract class Expr extends AST

case class RuleBuilder(initial: InitialStateSetter, surroundings: SurroundingSetter, 
    direction: DirectionSetter, finalState: FinalStateSetter) extends Expr
case class InitialStateSetter(state: State) extends Expr
case class SurroundingSetter(s1: DirectionAndContents = null, s2 : DirectionAndContents = null, 
    s3 : DirectionAndContents = null, s4: DirectionAndContents = null) extends Expr
case class DirectionSetter(dir: MoveDirection) extends Expr
case class FinalStateSetter(state: State) extends Expr
case class DirectionAndContents(contents: RelativeDescription, dir: MoveDirection)