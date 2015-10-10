# Design

asciibot

## Who is the target for this design, e.g., are you assuming any knowledge on the part of the language users?

Not one. We hope no one ever uses this. This is a example of what not to do.

Potentially a very visual person would want it. It can be nice to read,
although it sucks to write.

## Why did you choose this design, i.e., why did you think it would be a good idea for users to express the maze-searching computation using this syntax?

We chose this design because we thought it was goofy and would be fun to parse.
We've already done a bunch of parser combinator stuff in PLs so we thought it
would be nice to do something else.

## What behaviors are easier to express in your design than in Picobot's original design?  If there are no such behaviors, why not?

There are no behaviors that are easier to write. It is somewhat easier to read,
depending on the reader, which could be a nice property.

## What behaviors are more difficult to express in your design than in Picobot's original design? If there are no such behaviors, why not?

Writing any code is more difficult, becuase it requires extensive and
non-standard whitespace formatting to create correct code.

## On a scale of 1–10 (where 10 is "very different"), how different is your syntax from PicoBot's original design?

10

From a form perspective our syntax is entirely different. We have ascii art,
picobot had reasonable rule specifications.

From a AST / rule model perspective out language is very similar. We too have
numbered states and the user needs to specify the surrounding on all 4
directions.

## Is there anything you don't like about your design?

It is very hard to write.
