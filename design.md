# Design

## Who is the target for this design, e.g., are you assuming any knowledge on the part of the language users?

Lord of the Rings fans who are moderately well-versed in the lore of the world.

## Why did you choose this design, i.e., why did you think it would be a good idea for users to express the maze-searching computation using this syntax?

We liked the design we implemented last week. However, we thought it wasn't great as an internal DSL but would be much better as an external one. 

## What behaviors are easier to express in your design than in Picobot's original design?  If there are no such behaviors, why not?

We provide sensible defaults for contents of states and for state changes so that they can be omitted in certain cases (for instance, if you don't care what it is in a certain direction you shouldn't have to specify that you don't care).

## What behaviors are more difficult to express in your design than in Picobot's original design? If there are no such behaviors, why not?

None of them are more difficult conceptually, but most behaviours will require the programmer to be more verbose (which may be easier for some people, because it is more clear what is happening).

## On a scale of 1–10 (where 10 is "very different"), how different is your syntax from PicoBot's original design?

8. Robin made a good point that we underestimated how different the syntax was from Picobot's original design last week, plus we've changed even more now with adding defaults.

## Is there anything you don't like about your design?

Still so verbose.  Not quite as painfully slow as last time. 
