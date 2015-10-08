[Teams]: https://github.com/hmc-cs111-fall2015/piconot-external/wiki/Team-sign-ups
[API]: http://www.cs.hmc.edu/courses/2015/fall/cs111/picolib/index.html#package
[ParserCombinatorResource]: http://bitwalker.org/blog/2013/08/10/learn-by-example-scala-parser-combinators/
[ParserCombinatorAPI]: http://www.scala-lang.org/files/archive/api/2.11.7/scala-parser-combinators/#package

# Piconot: The externaling
###### _Submission deadline: Wednesday, Oct. 14 at 11:59pm_
###### _Critique deadline: Friday, Oct. 16 at 11:59pm_

Now that you've had a chance to push internal DSLs to the limit, let's try
external DSLs!

Implement an external version of Piconot. You can choose to implement any
version of Piconot you want (e.g., your original version from the previous
assignment, another team's version, a new version, etc.). Just make sure it's a
sufficiently large language that you can get good practice with parser
combinators and with the language architecture and implementation process we
discussed in class.

## Checklist
 - [ ] [Sign up for teams][Teams]. One team will have three people; all other 
    teams will have two people
 - [ ] Describe your design in `design.md` 
 - [ ] Before you implement the syntax, write the "empty room" program in
     `example-ideal.txt`
- [ ] Implement the syntax
     - [ ] Add files, as needed, to implement your syntax. Use the language
     architecture we discussed in class (more details below), and use parser
     combinators to implement your parser.
     - Include at least two example programs
        - [ ] `Empty.bot`
        - [ ] `RightHand.bot`
     - [ ] Provide instructions for how to run piconot on a file in `build.md`
     - [ ] Describe your implementation process in `evaluation.md`
  - [ ] Critique another team's design and implementation

## Design description and example program

Describe the design of the language you'll implement, in the file `design.md`
(see that file for instructions on what to write). If you're implementing the
same language you designed for the previous assignment, you can reuse the
contents of your file from that assignment (although it's probably a good idea
to revisit what you wrote to see if there's anything you would change).

Write the "empty room" program in `example-ideal.txt`.

## Implement your syntax

Implement the language. Use [parser combinators][ParserCombinatorAPI] and case
classes to implement the parser. Here's a [good article][ParserCombinatorResource] 
for learning a bit more about parser
combinators; there are many other ones on the web. Chapter 19 of _Scala for the
Impatient_ also covers parser combinators. For more complex syntaxes, the
_Language Implementation Patterns_ book may also be useful.

A note about building the given code: use sbt! If you don't, you'll have to jump
through many difficult hoops to get your code to compile against the parser
combinator library.

When implementing your language, use the architecture from class:

```
src/main 
 |-- scala
      |-- package name
           |-- parser
           |-- ir
           |-- semantics

src/test 
 |-- scala
      |-- package name
           |-- parser
           |-- ir
           |-- semantics
```
**Note:** You'll have to create these directories yourself -- they're not part
of the initial repository.

**Note:** Your intermediate representation might be data structures from the
`picolib` library, or it might be portions of your internal DSL, or a
combination of both.

### Errors

Your implementation should handle errors gracefully. Errors might include syntax
errors, programs that reference undefined rules, etc. **The team of three should
pay extra attention to error handling: the implementation should be extremely
robust, and the error messages should be clear and actionable.**

Giving good error messages for parsers is notoriously difficult. The `failure`, 
`positioned`, `phrase`, and `log` combinators (defined in the 
[scala.util.parsing.combinators.Parsers trait](http://www.scala-lang.org/files/archive/api/2.11.7/scala-parser-combinators/#scala.util.parsing.combinator.Parsers)) 
may be helpful.

## A running diary

As you work, comment on your experience in the file `evaluation.md`. In
particular, if you change your ideal syntax, you should describe what
changed and why you made the change (e.g., your original idea was too hard to
implement or it didn't match well with the library calls) You should also answer
the following questions: On a scale of 1–10 (where 10 is "a lot"), how much did
you have to change your syntax? 

## Provide instructions for building and running your code

In the `build.md` file provide instructions for how to build your software and
how to run it on a piconot file. A somewhat easy way to do is the following:
```
sbt run "maze-file bot-file"
```
Note that for your users to run your language this way, you'll have to design
your solution so that the `main` function takes and processes an argument that
contains the filename of the maze file and the filename of the picobot program.

Alternatively, you can build a stand-alone jar file, which users can execute:
  1. build the stand-alone .jar file by running `sbt assembly` 
  (and note the location of the jar file that sbt generates)
  2. run the software on a file by executing the command 
```
scala -cp path-to-jar-file name-of-class-with-main-function maze-file bot-file
```

## Peer-review another team's work

Comment on another team's design and implementation. You should look through
their grammars, pay special attention to their `evaluation.md` file, their
parser, and their intermediate representation. You might consider the following
questions:

  - What good insights about implementation did the team in `design.md`? Did
  you have any experiences that were similar to the team?
  - How nice is their implementation? Is it understandable? Clean? Modular?
  Extensible? What do you like about their code? Are there any particularly
  elegant ways they overcame implementation challenges?
  - Download their code and run their example programs. How easy was it to run? 
  Did the instructions work?
  - Modify the example programs to introduce errors. How robust is their
  implementation? What do you like about their error-handling? What can be improved?
  - Are there any implementation tricks that you can suggest to the team?
  Anything you see that might make the implementation easier or more like the
  original design?
