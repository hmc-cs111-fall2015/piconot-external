package picolang.ir

import scala.language.postfixOps

/**
 * @author dhouck apinson
 */

// We couldn’t get Enumeration to work
/** Relative and cardinal directions used for actions and facing */
abstract sealed class Direction(val absolute: Boolean) {
  /** Returns the direction to the "this" of where "that" is
   *  for instance:
   *     Left of(North) returns West
   *  Note that cardinal directions always return themselves  */
  def of(that: Direction): Direction = this
}
case object North extends Direction(true)
case object East extends Direction(true)
case object South extends Direction(true)
case object West extends Direction(true)
case object Forward extends Direction(false) {
  override def of(that: Direction): Direction = that
}
case object Back extends Direction(false) {
  override def of(that: Direction): Direction = Left of(Left of(that))
}
case object Left extends Direction(false) {
  override def of(that: Direction): Direction = that match {
    case North => West
    case West => South
    case South => East
    case East => North
    case Forward => Left
    case Left => Back
    case Back => Right
    case Right => Forward
  }
  
}
case object Right extends Direction(false) {
  override def of(that: Direction): Direction = Left of(Back of(that))
}

/** Actions are things the picobot can do from a given state */
abstract sealed class Action
case class Go(direction: Direction) extends Action {
  override def toString: String = "go " + (direction toString)
}
case class Turn(direction: Direction) extends Action {
  override def toString: String = "turn " + (direction toString)
}

/** A rule gives the picobot instructions on what to do 
 *  given certain surroundings */
class Rule(val surroundings: Map[Direction, Boolean], val actions: Seq[Action],
    val transition: Option[Name]) {
  override def toString: String = {
    val surroundingString = surroundings map {
      case (dir, open) =>
        val letter = (dir toString) charAt 0
        val description = if (open) "open" else "closed"
        letter + description
    }
    val actionString = actions mkString " "
    val transitionString = transition map {"transition \"" + _ + "\""} getOrElse ""
    s"rule $surroundingString $actionString $transitionString"
  }
}

/** States are collections of rules for various surroundings */
class State(val name: Name, val rules: List[Rule]) {
  override def toString: String = {
    "state \"" + name + "\"\n\t" + (rules mkString "\n\t") + "\n"
  }
}
