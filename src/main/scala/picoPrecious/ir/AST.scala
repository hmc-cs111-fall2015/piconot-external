package picoPrecious.ir

import picolib.semantics._

/**
 * @author Zoab
 */

sealed abstract class AST
sealed abstract class Expr extends AST

case class RuleBuilder(initialState: State, surroundings: SurroundingSetter, 
    direction: MoveDirection, finalState: State) extends Expr
case class SurroundingSetter(s1: DirectionAndContents = null, s2 : DirectionAndContents = null, 
    s3 : DirectionAndContents = null, s4: DirectionAndContents = null) extends Expr
case class DirectionAndContents(contents: RelativeDescription, dir: MoveDirection)