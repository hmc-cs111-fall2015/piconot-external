# Design

_The following responses are from the picobot-internal assignment._

## Who is the target for this design, e.g., are you assuming any knowledge on the part of the language users?
The target is the same target as the original picobot syntax. Our syntax is equally able to be described in one document. 

## Why did you choose this design, i.e., why did you think it would be a good idea for users to express the maze-searching computation using this syntax?
We chose this design because we like the existing picobot syntax but think that there are a few improvements that could be made. We didnt like that you have to specify the fact that you dont care about a direction (with a *). We think that you should just be able to not care. We also thought that explicitly specifying that you stay in the same state is unecessary and could be optional.

We also noticed that the specification of NEWS is very weird and decided that we should not care what order the directions are given at all. Our syntax allows for people to specify the direction and the state of the direction that they are looking for instead.

## What behaviors are easier to express in your design than in Picobot’s original design?  If there are no such behaviors, why not?
Rules where you only care about one specific direction are much easier to express. Also staying in the same state is easier to express by 2 characters :)

## What behaviors are more difficult to express in your design than in Picobot’s original design? If there are no such behaviors, why not?
If you want to specify states for all four directions then you have to type twice as many characters. We would argue, though, that our syntax is actually _clearer_ so we dont think its any more difficult to express.

## On a scale of 1–10 (where 10 is “very different”), how different is your syntax from PicoBot’s original design?
2

## Is there anything you don’t like about your design?
No, we like the picobot design and we are staying true to it.

