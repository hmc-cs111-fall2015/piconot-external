# Evaluation

We started with our syntax because it was different and fun. The only change we
really made was to allow multi-character state names (we didn't think about that
the first time) and excluding some special characters from the names.

Since our syntax is so non-linear and we couldn't think of how to describe it
using a standard grammar, we didn't use any parser combinators. This made it
somewhat easier because we didn't have to learn any libraries but leaves a lot
more for us to implement that we could miss. For example, while our code appears
to correctly parse a correct program, we are not sure that it will *not* accept
programs that are not correct and what kind of errors it will give. We are
trying to improve this but it's hard to check all the cases. If we were using
the library it would cover the edge cases automatically.
