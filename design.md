# Design

Each state is specified independently, with its rules in a block underneath the declaration. States are declared as


```state StateName```


where state names are arbitrary identifiers.
Rules are declared with the surroundings followed by an arrow, then zero or more `go` instructions with a direction (`North`, etc.), zero or more `turn` instructions with either `Left`, `Right`, `Back`, or the cardinal directions, followed by an optional `transition` instruction with a new state. The first state declared in a program is the start state.

Surroundings are represented by a list of one or more conditions, with the direction as a capital letter and specifiers "open" or "closed". Only one specifier per direction is allowed in any rule. If any directions are not specified, they will be assumed to be *. For example, Nopen is equivalent to x***, while Wclosed, Sopen is equivalent to *x*W. The directions can be specified in any order, and can either be cardinal ("NEWS") or relative ("FBLR" for "Forward, Back, Left, Right"). Finally, there is the Any surroundings, which is interpreted as **** and, if other rules are declared, must be declared last. If there are multiple non-Any rules that are not mutually exclusive, the first is chosen and the compiler will output a warning. Likewise, if the rules are not exhaustive, the compiler will also throw a warning. Since the compiler doesn't know which way Picobot is facing, mixing cardinal and relative directions in surroundings declarations will probably throw a warning.

Picobot now has an implicit cardinal direction it is facing at all times, and the default is `North`. The `go` instruction doesn't change the direction Picobot is facing, while the `turn` instruction does and does not move Picobot. Multiple `go` and `turn` directions can be specified at once, executed left to right, though they aren't allowed after a `transition` direction.

If a `go` instruction is absent, the language equates this with "X". Likewise, if no new state is specified, the language equates this with "stay in the same state" (and, of course, the user can put `transition StateName`).

## Who is the target for this design, e.g., are you assuming any knowledge on the part of the language users?

We assume that the users have basic knowledge of how Picobot operates, but not any knowledge of programming or DFAs.

## Why did you choose this design, i.e., why did you think it would be a good idea for users to express the maze-searching computation using this syntax?

The language is similar to the semantics of picobot, but it's easier to understand. The rules are limited enough to allow for more readable - yet still structured - instructions, but we weren't able to identify behaviors that Picobot allows that needed more syntactic sugar than we've already added.

## What behaviors are easier to express in your design than in Picobot’s original design?  If there are no such behaviors, why not?

All behaviors are easier, but turning is now easier and doesn't require multiple states to implement. Complex maneuvers are also easier to do because multiple `go` and `turn` instructions are allowed.

## What behaviors are more difficult to express in your design than in Picobot’s original design? If there are no such behaviors, why not?

The designs are so similar that there isn't anything that's particularly harder to express.

## On a scale of 1–10 (where 10 is “very different”), how different is your syntax from PicoBot’s original design?

5


State names are non-integer and don't need to be repeated, and users don't need to specify every direction in the surroundings, nor do they need to specify both a state transition and an action.
Now, instructions can use relative directions, allowing for more flexible states.

## Is there anything you don’t like about your design?

Nothing that isn't inherent to Picobot.