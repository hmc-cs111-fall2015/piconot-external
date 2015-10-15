name := "External Piconot"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"
libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value
libraryDependencies += "org.scalafx" % "scalafx_2.11" % "8.0.5-R5"
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.12.5" % "test"
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"

libraryDependencies ++= Seq("joda-time" % "joda-time" % "1.6",
        "org.apache.httpcomponents" % "httpclient" % "4.1.1",
        "javax.persistence" % "persistence-api" % "1.0",
        "org.scala-libs" %% "scalajpa" % "1.5",
        "oauth.signpost" % "signpost-core" % "1.2",
        "oauth.signpost" % "signpost-commonshttp4" % "1.2",
        "org.scala-lang" % "scala-actors" % "2.11.1",
        "org.scalatest" %% "scalatest" % "2.2.0" % "test",
        "org.specs2" %% "specs2" % "2.3.12" % "test",
        "org.scala-lang.modules" %% "scala-xml" % "1.0.2",
        "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.1",
        "org.json4s" %% "json4s-native" % "3.2.9",
        "junit" % "junit" % "4.7" % "test",
        "hsqldb" % "hsqldb" % "1.8.0.1" % "test",
        "org.hibernate" % "hibernate-entitymanager" % "3.4.0.GA",
        "org.slf4j" % "slf4j-simple" % "1.4.2")
