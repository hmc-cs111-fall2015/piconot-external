package picoPrecious.parser

import scala.language.implicitConversions
import java.lang.{ IllegalArgumentException => IAE }
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.scalatest.Assertions._
import picoPrecious.parser._
import picolib.semantics._

@RunWith(classOf[JUnitRunner])
class ParserTest extends Specification {

  "Rules " should {
    "have proper capitalization of the Shire when setting the final direction to" in {
      val s = "If you are holding weapon 1 and there is nothing towards the Undying Lands, then go towards the shire."      
      intercept[ProperNounException] {
        PicoParser(s)
      }      
      
      true
    }
    
    "have proper capitalization of the Lonely Mountain when setting the final direction to" in {
      val s1 = "If you are holding weapon 1 and there is nothing towards the Undying Lands, then go towards the lonely mountain."      
      intercept[ProperNounException] {
        PicoParser(s1)
      }      

      val s2 = "If you are holding weapon 1 and there is nothing towards the Undying Lands, then go towards the lonely Mountain."      
      intercept[ProperNounException] {
        PicoParser(s2)
      }    

      val s3 = "If you are holding weapon 1 and there is nothing towards the Undying Lands, then go towards the Lonely mountain."      
      intercept[ProperNounException] {
        PicoParser(s3)
      }      

      true
    }
    
    
    "have proper capitalization of the Undying Lands when setting the final direction to" in {
      val s1 = "If you are holding weapon 1 and there is nothing towards the Undying Lands, then go towards the Undying lands."      
      intercept[ProperNounException] {
        PicoParser(s1)
      }      

      val s2 = "If you are holding weapon 1 and there is nothing towards the Undying Lands, then go towards the undying Lands."      
      intercept[ProperNounException] {
        PicoParser(s2)
      }    

      val s3 = "If you are holding weapon 1 and there is nothing towards the Undying Lands, then go towards the undying lands."      
      intercept[ProperNounException] {
        PicoParser(s3)
      }      

      true
    }
    
    "have proper capitalization of Mordor when setting the final direction to" in {
      val s = "If you are holding weapon 1 and there is nothing towards the Undying Lands, then go towards mordor."      
      intercept[ProperNounException] {
        PicoParser(s)
      }      
      
      true
    }
    
    "use definite articles when setting the final direction to the Shire" in {
      val s1 = "If you are holding weapon 1 and there is nothing towards the Undying Lands, then go towards Shire."      
      intercept[DefiniteArticleException] {
        PicoParser(s1)
      }      
      
      val s2 = "If you are holding weapon 1 and there is nothing towards the Undying Lands, then go towards shire."      
      intercept[DefiniteArticleException] {
        PicoParser(s2)
      }      
      
      true
    }
    
    "not use definite articles when setting the final direction to Mordor" in {
      val s1 = "If you are holding weapon 1 and there is nothing towards the Undying Lands, then go towards the Mordor."      
      intercept[DefiniteArticleException] {
        PicoParser(s1)
      }      
      
      val s2 = "If you are holding weapon 1 and there is nothing towards the Undying Lands, then go towards the mordor."      
      intercept[DefiniteArticleException] {
        PicoParser(s2)
      }      
      
      true
    }
    
    "use definite articles when setting the final direction to the Undying Lands" in {
      val s1 = "If you are holding weapon 1 and there is nothing towards the Undying Lands, then go towards Undying Lands."      
      intercept[DefiniteArticleException] {
        PicoParser(s1)
      }      
      
      val s2 = "If you are holding weapon 1 and there is nothing towards the Undying Lands, then go towards undying Lands."      
      intercept[DefiniteArticleException] {
        PicoParser(s2)
      }      
      
      val s3 = "If you are holding weapon 1 and there is nothing towards the Undying Lands, then go towards Undying lands."      
      intercept[DefiniteArticleException] {
        PicoParser(s3)
      }      
      
      val s4 = "If you are holding weapon 1 and there is nothing towards the Undying Lands, then go towards undying lands."      
      intercept[DefiniteArticleException] {
        PicoParser(s4)
      }   
      
      true
    }
    
    "use definite articles when setting the final direction to the Lonely Mountain" in {
      val s1 = "If you are holding weapon 1 and there is nothing towards the Undying Lands, then go towards Lonely Mountain."      
      intercept[DefiniteArticleException] {
        PicoParser(s1)
      }      
      
      val s2 = "If you are holding weapon 1 and there is nothing towards the Undying Lands, then go towards lonely Mountain."      
      intercept[DefiniteArticleException] {
        PicoParser(s2)
      }      
      
      val s3 = "If you are holding weapon 1 and there is nothing towards the Undying Lands, then go towards Lonely mountain."      
      intercept[DefiniteArticleException] {
        PicoParser(s3)
      }      
      
      val s4 = "If you are holding weapon 1 and there is nothing towards the Undying Lands, then go towards lonely mountain."      
      intercept[DefiniteArticleException] {
        PicoParser(s4)
      }   
      
      true
    }
  
    "have proper capitalization of the Shire when indicating the contents of a location" in {
      val s = "If you are holding weapon 1 and there is nothing towards the shire, then go towards the Shire."      
      intercept[ProperNounException] {
        PicoParser(s)
      }      
      
      true
    }
    
    "have proper capitalization of the Lonely Mountain when indicating the contents of a location" in {
      val s1 = "If you are holding weapon 1 and there is nothing towards the lonely Mountain, then go towards the Lonely Mountain."      
      intercept[ProperNounException] {                                       
        PicoParser(s1)                                                       
      }                                                                      
                                                                             
      val s2 = "If you are holding weapon 1 and there is nothing towards the Lonely mountain, then go towards the Lonely Mountain."      
      intercept[ProperNounException] {                                       
        PicoParser(s2)                                                       
      }                                                                      
                                                                             
      val s3 = "If you are holding weapon 1 and there is nothing towards the lonely mountain, then go towards the Lonely Mountain."      
      intercept[ProperNounException] {
        PicoParser(s3)
      }      

      true
    }
    
    
    "have proper capitalization of the Undying Lands when indicating the contents of a location" in {
      val s1 = "If you are holding weapon 1 and there is nothing towards the undying lands, then go towards the Undying Lands."      
      intercept[ProperNounException] {
        PicoParser(s1)
      }      

      val s2 = "If you are holding weapon 1 and there is nothing towards the undying Lands, then go towards the Undying Lands."      
      intercept[ProperNounException] {
        PicoParser(s2)
      }    

      val s3 = "If you are holding weapon 1 and there is nothing towards the Undying lands, then go towards the Undying Lands."      
      intercept[ProperNounException] {
        PicoParser(s3)
      }      

      true
    }
    
    "have proper capitalization of Mordor when indicating the contents of a location" in {
      val s = "If you are holding weapon 1 and there is nothing towards mordor, then go towards Mordor."      
      intercept[ProperNounException] {
        PicoParser(s)
      }      
      
      true
    }
    
    "use definite articles when indicating the contents of a location the Shire" in {
      val s1 = "If you are holding weapon 1 and there is nothing towards Shire, then go towards the Shire."      
      intercept[DefiniteArticleException] {
        PicoParser(s1)
      }      
      
      val s2 = "If you are holding weapon 1 and there is nothing towards shire, then go towards the Shire."      
      intercept[DefiniteArticleException] {
        PicoParser(s2)
      }      
      
      true
    }
    
    "not use definite articles when indicating the contents of a location Mordor" in {
      val s1 = "If you are holding weapon 1 and there is nothing towards the Mordor, then go towards Mordor."      
      intercept[DefiniteArticleException] {
        PicoParser(s1)
      }      
      
      val s2 = "If you are holding weapon 1 and there is nothing towards the mordor, then go towards Mordor."      
      intercept[DefiniteArticleException] {
        PicoParser(s2)
      }      
      
      true
    }
    
    "use definite articles when indicating the contents of a location the Undying Lands" in {
      val s1 = "If you are holding weapon 1 and there is nothing towards Undying Lands, then go towards the Undying Lands."      
      intercept[DefiniteArticleException] {                                                                
        PicoParser(s1)                                                                                     
      }                                                                                                    
                                                                                                           
      val s2 = "If you are holding weapon 1 and there is nothing towards Undying Lands, then go towards the Undying Lands."      
      intercept[DefiniteArticleException] {                                                                
        PicoParser(s2)                                                                                     
      }                                                                                                    
                                                                                                           
      val s3 = "If you are holding weapon 1 and there is nothing towards Undying Lands, then go towards the Undying Lands."      
      intercept[DefiniteArticleException] {
        PicoParser(s3)
      }      
      
      val s4 = "If you are holding weapon 1 and there is nothing towards Undying Lands, then go towards the Undying Lands."      
      intercept[DefiniteArticleException] {
        PicoParser(s4)
      }   
      
      true
    }
    
    "use definite articles when indicating the contents of a location the Lonely Mountain" in {
      val s1 = "If you are holding weapon 1 and there is nothing towards Lonely Mountain, then go towards the Lonely Mountain."      
      intercept[DefiniteArticleException] {                                                  
        PicoParser(s1)                                                                       
      }                                                                                      
                                                                                             
      val s2 = "If you are holding weapon 1 and there is nothing towards lonely Mountain, then go towards the Lonely Mountain."      
      intercept[DefiniteArticleException] {                                                
        PicoParser(s2)                                                                     
      }                                                                                    
                                                                                           
      val s3 = "If you are holding weapon 1 and there is nothing towards Lonely mountain, then go towards the Lonely Mountain."      
      intercept[DefiniteArticleException] {                                                  
        PicoParser(s3)                                                                       
      }                                                                                      
                                                                                             
      val s4 = "If you are holding weapon 1 and there is nothing towards lonely mountain, then go towards the Lonely Mountain."      
      intercept[DefiniteArticleException] {
        PicoParser(s4)
      }   
      
      true
    }
  }
}
