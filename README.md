Remote JavaScript console for Scala apps
========================================

=== Installation ===

In sbt:

```
resolvers += "s4gnuplot gihub maven repo" at "https://github.com/Rogach/org.rogach/raw/master/"

libraryDependencies += "org.rogach" % "rjs4scala" % "0.1"
```

Or you can download jar manually from https://github.com/Rogach/org.rogach/raw/master/org/rogach/rjs4scala/0.1/rjs4scala-0.1.jar

=== Example ===

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
```

In case you don't like JavaScript, you can provide your own handler:

```scala
val myHandler: String => Some[String] = line => line.toInt * 2
org.rogach.misc.Rjs.endpoint(port = 8093, handler = myHandler)
```

To end the conversation from that handler, just throw org.rogach.misc.Rjs.ConversationEnded.

