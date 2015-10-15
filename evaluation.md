# Evaluation

We used our old abstract syntax tree as our intermediate representation.
Everything for example-ideal.txt and design.md was added over (except we now allow for mixed cardinal and relative directions).

Next, we copied over some of our package code and the semantics functions from our old DSL.

We started moving to a correct package layout.

Thus far, we've implemented parsers for states, actions, transitions, directions, a list of surroundings and individual surroundings, as well as a few other things.

For rules, we need to find out how to handle multiple actions and one or more transitions, since allowing [0 or more actions] and [optional transition] lets users input no instructions.

We also need to make sure "Nopen" and such are valid and check if the parser will allow for "N open" as it is now. Basically, we need to know how "~" handles whitespace.

We found that "N open" is allowed, and decided to let users use this notation.

Currently, "goNorthtransitionSweep" is allowed, so we need to add whitespace requirements.

We did this by using the `ident` parser even for keywords.

We did not get as much error handling done as ideal.
