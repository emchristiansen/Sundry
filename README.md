#Sundry

Sundry contains some types and methods which I find useful; in spirit it
is similar to [Grizzled Scala](https://github.com/bmc/grizzled-scala).

[![Build Status](https://travis-ci.org/emchristiansen/Sundry.png)](https://travis-ci.org/emchristiansen/Sundry)

##Installation

You can use Sundry in your SBT project by simply adding the following dependency to your build file:

```scala
libraryDependencies += "st.sparse" %% "sundry" % "0.1-SNAPSHOT"
```

You also need to add the Sonatype "snapshots" repository resolver to your build file:

```scala
resolvers += Resolver.sonatypeRepo("snapshots")
```
