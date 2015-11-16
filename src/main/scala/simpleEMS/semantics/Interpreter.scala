package simpleEMS.semantics

import simpleEMS.ir._

/**
 * ExprInterpreter can evaluate an Expr
 */
object ExprInterpreter {
  /** evaluating an expression **/
  def eval(expr: Expr): Value = evalE(expr, ρ0, σ0)

  def evalE(expr: Expr, ρ: Environment, σ: Store): Value = expr match {
    case Num(i)            ⇒ i
    case x: Var            ⇒ σ(ρ(x))
    case Plus(left, right) ⇒ evalE(left, ρ, σ) + evalE(right, ρ, σ)
    case Sub(left, right)  ⇒ evalE(left, ρ, σ) - evalE(right, ρ, σ)
    case Mult(left, right) ⇒ evalE(left, ρ, σ) * evalE(right, ρ, σ)
    case Div(left, right)  ⇒ evalE(left, ρ, σ) / evalE(right, ρ, σ)
  }
}

/**
 * StmtInterpreter can evaluate a Stmt
 */

object StmtInterpreter {
  import ExprInterpreter.evalE

  /** evaluating a statement **/
  def eval(stmt: Stmt): Result = evalS(stmt, ρ0, σ0)

  def evalS(stmt: Stmt, ρ: Environment, σ: Store): Result = stmt match {
    case Print(e)         ⇒ evalPrint(e, ρ, σ)
    case Block(stmts)     ⇒ evalBlock(stmts, ρ, σ)
    case If0(e, s_t, s_f) ⇒ evalIf0(e, s_t, s_f, ρ, σ)
    case Set(x, e)        ⇒ evalAssign(x, e, ρ, σ)
    case Update(x, e)     ⇒ evalUpdate(x, e, ρ, σ)    
    case f: FuncDef       ⇒ evalFuncDef(f, ρ, σ)   
    case Call(f, args)    ⇒ evalCall(f, args, ρ, σ)    
  }

  /** print **/
  def evalPrint(expr: Expr, ρ: Environment, σ: Store): Result = {
    // (1) evaluate the expression
    val v = evalE(expr, ρ, σ)

    // (2) print the result
    println(v)
    
    (ρ, σ) // printing doesn't affect the result
  }
  
}