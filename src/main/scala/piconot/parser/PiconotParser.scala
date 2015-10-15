package piconot.parser

import scala.util.parsing.combinator._
import piconot.ir._
import piconot.ir.sugar._
import picolib.semantics._

object PiconotParser extends JavaTokenParsers with PackratParsers {

    // parsing interface
    def apply(s: String): ParseResult[AST] = parseAll(multiTransformer, s)
    
    // transformer
    lazy val transformer: PackratParser[Transformer] =
      (  move~"else"~transformers ^^ {case mov~"else"~trans => new ElseTransformerBasic(mov, trans)}  
         | move~transformers~"else"~transformers ^^ {case mov~trans1~"else"~trans2 => new ElseTransformerComplex(mov, trans1, trans2)} 
         | augment~separator~transformers ^^ {case aug~s~trans => new AugmentTransformer(aug, trans)}
         | augment~"{"~multiTransformer~"}" ^^ {case aug~"{"~mTrans~"}" => new AugmentTransformer(aug, new BracedTransformers(mTrans))}
         | augment ^^ {aug => new BaseTransformer(aug)}
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
      (  move ^^ { move => move}
         | "stay" ^^ { _ => new Stay()}
         | "state"~ident ^^ {case "state"~s => new StateDef(s)}
         | dir~restrictdef ^^ {case d~r => new Restrict(d,r)}
         | ident ^^ {case s => new MoveState(s)}
      )
      
    lazy val move: PackratParser[Move] =
      ( "move"~dir ^^ {case "move"~d => 
        println("Move dir:", d)
        new Move(d)} )
      
    //separator
    lazy val separator: PackratParser[Boolean] = 
      ( ("," | ":" | "->") ^^ {_ => true})
    
    //newline
    lazy val newline: PackratParser[Boolean] =
        ( ";"  ^^ {_ => true}
        | "\n" ^^ {_ => true}
        )
     
    //restrictdef
    lazy val restrictdef: PackratParser[RelativeDescription] =
      ( //environment?
       ("open" | "free") ^^ {_ => Open} 
       | ("blocked" | "closed") ^^ {_ => Blocked}
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