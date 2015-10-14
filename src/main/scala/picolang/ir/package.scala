package picolang

/**
 * @author apinson
 */
package object ir {
  /** The name of a state */
  type Name = String
  /** The abstract syntax tree for our language */
  type AST = Seq[State]
}