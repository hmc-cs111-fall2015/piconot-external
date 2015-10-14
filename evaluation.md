# Evaluation

##Running Commentary
We decided that we wanted to keep our language idea from last week. All the reasons that we wanted to create that language were still true, and this provided us with an opportunity to realize the language that we had initially conceived. We started off feeling pretty overwhelmed, as always. The key was writing out a grammar ourselves in BNF to show us where we needed to start from and where we needed to go and how we were gonna get there. Essentially the BNF became the parsing functions.

We pretty much were able to code straight through without any problems (that took >5 minutes) until we got to testing. At this point we ran into a couple of of problems. One, when our parser was reading lines, if the end state was left off then it would read the next start state as the previous rule's end state. We tried to use a new line character to be a line ender but because of the parser's automatic whitespace trimming that made it impossible. We decided that semicolons were our best option.

When it came time to running we got through the parsing but are getting some weird JFX error that we cant understand. Going to ask Prof Ben in class on wednesday. Has something to do with stage/mainstage but we dont understand it and there is nothing on google.


## On a scale of 1–10 (where 10 is "a lot"), how much did you have to change your syntax? 
1, we had to add semicolons


## On a scale of 1–10 (where 10 is "very difficult"), how difficult was it to map your syntax to the provided API?
ezpz