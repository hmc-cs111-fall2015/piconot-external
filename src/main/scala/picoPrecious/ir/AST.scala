package picoPrecious.ir

import picolib.semantics._

/**
 * @author Zoab
 */

sealed abstract class AST
sealed abstract class Expr extends AST

case class RuleBuilder(initialState: State, surroundings: SurroundingSetter, 
    direction: MoveDirection, finalState: State) extends Expr
case class SurroundingSetter(directions: List[DirectionAndContents]) extends Expr
case class DirectionAndContents(contents: RelativeDescription, dir: MoveDirection)