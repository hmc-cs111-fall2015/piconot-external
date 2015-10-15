# Building and running Piconot

The main class (`picolang.runner`) takes in two arguments: the maze and the bot. These are provided with the command line arguments `--maze=<maze file>` and `--bot=<bot file>`. Thus, you can run this program with

```
sbt "run --maze=maze-file --bot=bot-file"
```

Alternatively, you can build a stand-alone jar file, which you can then execute:
  1. build the stand-alone `.jar` file by running `sbt assembly` 
  (and note the location of the jar file that sbt generates)
  2. run the software on a file by executing the command 
```
scala -cp path-to-jar-file picolang.runner --maze=maze-file --bot=bot-file
```
Note: Much of this file was copied from README.md
