package picolang

/**
 * @author apinson dhouck
 */
object semantics {
  import picolib.{semantics => lib}
  
  protected def toStateName(name: Name, dir: Direction): String = name + " " + dir.toString
  
  /** Converts the surroundings of a rule to the format expected by picolib */
  protected def surrToPicolibSurr(surroundings: Map[Direction, Boolean], dir: Direction): 
    Option[lib.Surroundings] = {
    
    val (cardinalDirs, relativeDirs) = surroundings.partition(_._1.absolute)
    val cardinalEquiv = relativeDirs.map{p => p._1.of(dir) -> p._2}
    val contradictory = cardinalDirs exists {pair: (Direction,Boolean) =>
      (cardinalEquiv get (pair._1)) == Some(!(pair._2))
    }
    if (contradictory) None else {
      val combined = (cardinalDirs++cardinalEquiv).mapValues{if (_) lib.Open else lib.Blocked}
      
      Some(lib.Surroundings(combined.getOrElse(North, lib.Anything),
                            combined.getOrElse(East, lib.Anything),
                            combined.getOrElse(West, lib.Anything),
                            combined.getOrElse(South, lib.Anything)))
    }
  }
  
  /** Converts relative actions to cardinal actions
   *  
   *  The list of actions is converted to a list of go actions and a single turn */
  protected def cardinalizeActions(ourActions: List[Action], facing: Direction)
      : (List[Go], Turn) = {
    ourActions match {
      case Nil => (Nil, Turn(facing))
      case Go(dir)::rest =>
        val (restActions, turnDir) = cardinalizeActions(rest, facing)
        (Go(if (dir.absolute) dir else dir of(facing))::restActions, turnDir)
      case Turn(dir)::rest =>
        cardinalizeActions(rest, dir of(facing))
    }
  }
  /** Converts Direction to a picolib direction */
  protected def dirToPicolibDir(dir: Direction): lib.MoveDirection = {
    dir match {
      case North => lib.North
      case East => lib.East
      case South => lib.South
      case West => lib.West
      case _ =>
        throw new IllegalArgumentException("Tried to treat relative direction as absolute")
    }
  }
  
  /** Given a list of directions, creates a list of picolib rules to go in
   *  each direction in sequence */
  protected def multipleDirsToRulesList(statePrefix: String, stepNumber: Int, directions: List[lib.MoveDirection], end: lib.State)
      : List[lib.Rule] = {
    val currentState = lib.State(statePrefix + stepNumber)
    val nextState =
      if ((directions length) == 1) end else lib.State(statePrefix + (stepNumber + 1))
    val anySurroundings = lib.Surroundings(lib.Anything,lib.Anything,lib.Anything,lib.Anything)
    directions match {
      case Nil => Nil
      case (dir::rest) =>
        val rule = lib.Rule(currentState, anySurroundings, dir, nextState)
        rule::(multipleDirsToRulesList(statePrefix, stepNumber + 1, rest, end))
    }
  }
  /** Converts a Rule to an equivalent sequence of picolib rules */
  protected def ruleToPicolibRules(rule: Rule, stateName: Name, facing: Direction):List[lib.Rule] = {
    val startState = toStateName(stateName, facing)
    surrToPicolibSurr(rule.surroundings, facing) match {
      case None => List()
      case Some(surroundings) => 
        val (actions, Turn(finalDir)) = cardinalizeActions(rule.actions.toList.reverse, facing)
        val endState = toStateName(rule.transition getOrElse(stateName), finalDir)
        val dirsToGo = actions map {case Go(dir) => dirToPicolibDir(dir)}
        dirsToGo match {
          case Nil => List(lib.Rule(lib.State(startState),
                                    surroundings,
                                    lib.StayHere,
                                    lib.State(endState)))
          case dir::Nil => List(lib.Rule(lib.State(startState),
                                         surroundings,
                                         dir,
                                         lib.State(endState)))
          case (dir::more) =>
            val statePrefix = startState + s" $surroundings Step "
            val rule = lib.Rule(lib.State(startState), surroundings, dir, lib.State(statePrefix + "2"))
            rule::multipleDirsToRulesList(statePrefix, 2, more, lib.State(endState))
        }
    }
  }
  
  /** Converts an AST to a list of picolib rules */
  protected def toRule(ast: AST): List[lib.Rule] = {
    ast.reverse flatMap {state =>
      state.rules.reverse flatMap {(rule: Rule) =>
        List(North, East, South, West) flatMap {dir =>
          ruleToPicolibRules(rule, state.name, dir)
        }
      }
    } toList
  }
}