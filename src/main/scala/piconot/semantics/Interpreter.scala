package piconot

/**
 * @author mvalentine
 */

import piconot.ir._
import picolib.semantics._
import java.io.File
import scalafx.application.JFXApp

import picolib.maze.Maze

package object semantics {
  
  def picobot(mazename: String)(rs: Seq[Rule]*): List[Rule] = {
    val rules = rs.flatten.toList
    
    val maze = Maze("resources" + File.separator + s"${mazename}.txt")
    object RuleBot extends Picobot(maze, rules)
      with TextDisplay with GUIDisplay
    RuleBot.run()
    var stage = RuleBot.mainStage
    
    rules
  }
  
  def eval(ast: AST): Unit = {
    val rules = evalTransformers(ast)
    picobot("maze")(rules)
  }
  
  def evalTransformers(ast: AST): Seq[Rule] = ast match {
    case BaseTransformer(aug: Augment) => Seq(applyAugment(aug, DefaultRule))
    case AugmentTransformer(aug: Augment, trans: Transformers) =>
        evalTransformers(trans).map {rule => applyAugment(aug, rule)}
        
    case BaseTransformers(trans: Transformer) => evalTransformers(trans)
    case BracedTransformers(multiTrans: MultiTransformers) => evalTransformers(multiTrans)
    
    case SingleMultiTransformers(t: Transformer) => evalTransformers(t)
    case MutlipleMultiTransformers(transL, multiT) => 
      evalTransformers(transL) ++ evalTransformers(multiT)
    case _ => Seq()
    
  }
  
  def applyAugment(aug: Augment, rule: Rule): Rule = aug match {
    case Move(dir) => rule.copy(moveDirection = dir)
    case Stay() => rule.copy(moveDirection = StayHere)
    case Restrict(dir, relDisc) => 
      rule.copy(
        surroundings = dir match {
          case North => rule.surroundings.copy(north = relDisc)
          case South => rule.surroundings.copy(south = relDisc)
          case East => rule.surroundings.copy(east = relDisc)
          case West => rule.surroundings.copy(west = relDisc)
        })
    case StateDef(stateName: String) => rule.copy(startState = new State(stateName))
    case MoveState(stateName: String) => rule.copy(endState = new State(stateName))
  }
  
  val DefaultSurroundings: Surroundings = Surroundings(Anything, Anything, Anything, Anything)

  val UndefinedStateString: String = "!!__ UN DE FI NE D __!!"
  val UndefinedState: State = State(UndefinedStateString)
  
  val DefaultRule: Rule = Rule(UndefinedState, 
                          DefaultSurroundings, 
                          StayHere, 
                          UndefinedState)
}