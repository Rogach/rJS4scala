Remote JavaScript console for Scala apps
========================================

Installation
------------

In sbt:

```
resolvers += "Rogach's maven repo" at "https://github.com/Rogach/org.rogach/raw/master/"

libraryDependencies += "org.rogach" % "rjs4scala" % "0.1"
```

Or you can download jar manually from https://github.com/Rogach/org.rogach/raw/master/org/rogach/rjs4scala/0.1/rjs4scala-0.1.jar

Example
-------

Start an endpoint from your application:

```scala
// object, that would be available on the remote console
object Something {
  val a = 5
  def doSmth = println("A")
}
import org.rogach.misc.Rjs
Rjs.jsEndpoint(Map("someObj" -> Something), port = 8093)
```

and access it (via telnet, nc, ..):

```
> nc localhost 8093
1 + 2
3
someObj.a()
5
someObj.doSmth() // "A" would be printed on the server output
```

In case you don't like JavaScript, you can provide your own handler:

```scala
val myHandler: String => Some[String] = line => Some(line.toInt * 2)
org.rogach.misc.Rjs.endpoint(port = 8093, handler = myHandler)
```

To end the conversation from that handler, just throw org.rogach.misc.Rjs.ConversationEnded.

persistent-connect.rb
---------------------

Also I wrote up a simple script, that connects to the given port and reconnects every time your application is restarted.

Together with sbt-revolver plugin, it allows for super-fast turnaround - you hit "save" in your editor -> sbt recompiles & restarts -> app opens a port -> persistent-connect.rb connects to the port, sends some commands -> when you reach for your app, it has already done something useful.

Usage:

```
./persistent-connect.rb port [ commands to run ]
```