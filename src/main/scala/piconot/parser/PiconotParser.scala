package piconot.parser

import scala.util.parsing.combinator._
import piconot.ir._
import piconot.ir.sugar._
import picolib.semantics._

object PiconotParser extends JavaTokenParsers with PackratParsers {

    // parsing interface
    def apply(s: String): ParseResult[AST] = parseAll(transformer, s)
    
    // transformer
    lazy val transformer: PackratParser[Transformer] =
      (  augment~separator~transformers ^^ {case aug~s~trans => new AugmentTransformer(aug, trans)}
          
      )
    
    //transformers
    lazy val transformers: PackratParser[Transformers] = 
      ( transformer ^^ {trans => new BaseTransformers(trans)}
      | "{"~multiTransformer~"}" ^^ {case "{"~mTrans~"}" => new BracedTransformers(mTrans)}
      )
    
    //multiTransformer
    lazy val multiTransformer: PackratParser[MultiTransformers] =
      ( transformer~newline~multiTransformer ^^ {case trans~nL~mTrans => new MutlipleMultiTransformers(trans, mTrans)}
      | transformer ^^ {trans => new SingleMultiTransformers(trans)}
      )
    
    // augment
    lazy val augment: PackratParser[Augment] =
      (  "move"~dir ^^ {case "move"~d => new Move(d)}
         | "stay" ^^ { _ => new Stay()}
         | "state"~ident ^^ {case "state"~s => new StateDef(s)}
         | ident ^^ {case s => new MoveState(s)}
      )
      
    //separator
    lazy val separator: PackratParser[Unit] = 
      ( ","    ^^ {_ => }
      | "lw"   ^^ {_ => }
      | ":"    ^^ {_ => }
      )
    
    //newline
    lazy val newline: PackratParser[Unit] =
        ( ";"  ^^ {_ => }
        | "ln" ^^ {_ => }
        )
     
    //restrictdef
    lazy val restrictdef: PackratParser[Unit] =
      ( //environment?
       "" ^^ {_ => } //placeholder
      )
      
    // dir
    lazy val dir: PackratParser[MoveDirection] =
      ( ("north" | "n" | "up") ^^ {_ => North}
        | ("east" | "e" | "right") ^^ {_ => East}
        | ("south" | "s" | "down") ^^ {_ => South}
        | ("west" | "w" | "left") ^^ {_ => West} )
      
      
//    // expressions
//    lazy val expr: PackratParser[Expr] = 
//      (   "-" ~ expr ^^ {case "-"~e => new Neg(e) }
//        |  expr~"+"~fact ^^ {case l~"+"~r => l |+| r}
//        |  expr~"-"~fact ^^ {case l~"-"~r => l |-| r}
//        | "(" ~ expr ~ ")" ^^ {case "("~e~")" => e}
//        | fact )
//        
//    lazy val fact: PackratParser[Expr] =
//      ( fact~"*"~term ^^ {case l~"*"~r => l |*| r}
//        | term )
//      
//    // factors
//    lazy val term: PackratParser[Expr] =
//      number
//      
//    // numbers
//    def number: Parser[Num] = wholeNumber ^^ {s ⇒ Num(s.toInt)}
    
 }