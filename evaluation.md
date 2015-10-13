# Evaluation

First we had to drop ordinal numbers once again - we found that parsing and understanding those
posed far too great a challenge, and it was simply not worth the additional implementation effort.

We had to give up on limiting the number of directions they could specify with the parser - while 
we tried to limit them to a maximum of four we couldn't seem to get the `PackratParser` to work.
We added this functionality with the interpreter, but it would have been nice to know while parsing.

We got rid of a lot of our intermediate representation objects in favor of objects from
`picolib.semantics` because we found them to be unnecessary.

We force them to use "there is" or "there are" (as grammatically appropriate) instead of simply
saying "and Orcs towards the Shire".  We found the additional implementation effort to ensure
that they do say "there is" or "there are" for the first time but not the rest be not worthwhile,
and we felt the grammatically correct aspect means a lot for our language (given the natural
language nature of our DSL).

We had to give up on a number of parse-time semantics errors because we weren't sure of how to 
do them; instead we let the interpreter raise errors when bad things happen.

## How much did you have to change your syntax?

About a 4.

We found that we didn't have to give up nearly as many things as we did with the internal DSL.

In fact we added a feature that was not present in the ideal program - we added defaulting to
staying in the same state, which wasn't in example-ideal.txt.
