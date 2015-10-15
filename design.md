# Design

## Who is the target for this design, e.g., are you assuming any knowledge on the part of the language users?

We are targetting CS5 students with this design. We assume the user has a basic understanding of programming.

## Why did you choose this design, i.e., why did you think it would be a good idea for users to express the maze-searching computation using this syntax?

We chose this design so that users can write more readable picobot code.
This way, it is easier to interpret and debug!

## What behaviors are easier to express in your design than in Picobot's original design?  If there are no such behaviors, why not?

It is easier to issue complex commands. In this language, the else statement allows users to write about choices explicitly. "Can I go this way, if I can't, can I go some other way?"

## What behaviors are more difficult to express in your design than in Picobot's original design? If there are no such behaviors, why not?

It is more tedious to express behaviors that depend on many unit movements. (e.g. the case where someone explicitly wants picobot to move up, left, down, right, right, left, right)
These short chained states will be hard to read since our syntax spaces out each rule more.

## On a scale of 1–10 (where 10 is "very different"), how different is your syntax from PicoBot's original design?

7-8; The overall ideas about states and cardinal directions still exist. However, someone who uses our syntax would not be able to be able to immediately write in picobot language and vice versa.

## Is there anything you don't like about your design?

We know we can toggle Scala parser to ignore whitespace or to read whitespace -- so we made the decision to ignore white space and seperate rules in the same state with commas to save implementation time. I don't like that because it makes the syntax messier by requiring commas or semi-colons but parsing white-space was not something we were willing to do. (Implementor vs User!)