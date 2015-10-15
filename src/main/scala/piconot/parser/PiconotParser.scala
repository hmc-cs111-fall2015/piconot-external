package piconot.parser

import scala.util.parsing.combinator._
import piconot.ir._
import piconot.ir.sugar._
import picolib.semantics._

object PiconotParser extends JavaTokenParsers with PackratParsers {
    // parsing interface
    def apply(s: String): ParseResult[AST] = {
      parseAll(multiTransformer, s)
    }
    
    def word(s: String): Parser[String] = ident.filter(_ == s)
    
    val reserved = "else move up down left right north south east west n s e w open free blocked closed".split(" ")
    //lazy val ident2: PackratParser[String] = ident.filter(s => !(reserved contains s))
    
    def ident2: PackratParser[String] = new PackratParser[String] {
      def apply(in: Input): ParseResult[String] = {
        val reserved = List("else")
        val parse = ident(in)
        parse match {
          case Success(word, _) => if (reserved contains word) {
              Failure(s"${word} is a reserved word. It cannot be used as a state name.", in)
            } else {
              parse
            }
          case _ => parse
        }
      }
    }
    
    // transformer
    lazy val transformer: PackratParser[Transformer] =
      (  move~word("else")~transformers ^^ {case mov~word("else")~trans => new ElseTransformerBasic(mov, trans)}  
         | move~transformers~word("else")~transformers ^^ {case mov~trans1~word("else")~trans2 => new ElseTransformerComplex(mov, trans1, trans2)} 
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
         | word("stay") ^^ { _ => new Stay()}
         | word("state")~ident2 ^^ {case word("state")~s => new StateDef(s)}
         | dir~restrictdef ^^ {case d~r => new Restrict(d,r)}
         | ident2 ^^ {case s => new MoveState(s)}
      )
      
    lazy val move: PackratParser[Move] =
      ( word("move")~dir ^^ {case word("move")~d => 
        println("Move dir:", d)
        new Move(d)} )
      
    //separator
    lazy val separator: PackratParser[Boolean] = 
      ( ("," | ":" | "->") ^^ {_ => true})
    
    //newline
    lazy val newline: PackratParser[Boolean] =
        ( ";"  ^^ {_ => true}
        //| "\n" ^^ {_ => true}
        )
     
    //restrictdef
    lazy val restrictdef: PackratParser[RelativeDescription] =
      ( //environment?
       (word("open") | word("free")) ^^ {_ => Open} 
       | (word("blocked") | word("closed")) ^^ {_ => Blocked}
      )
      
    // dir
    lazy val dir: PackratParser[MoveDirection] =
      ( (word("north") | word("n") | word("up")) ^^ {_ => North}
        | (word("east") | word("e") | word("right")) ^^ {_ => East}
        | (word("south") | word("s") | word("down")) ^^ {_ => South}
        | (word("west") | word("w") | word("left")) ^^ {_ => West} )
      
      
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