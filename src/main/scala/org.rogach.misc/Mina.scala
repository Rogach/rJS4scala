package org.rogach.misc

import java.net.InetSocketAddress
import java.nio.charset.Charset
import java.net._
import java.io._

object Rjs {
  def main(args:Array[String]) {
    jsEndpoint(Map("mina" -> Mina))
  }
  
  case class ConversationEnded(farewell:String) extends Exception
  
  def jshandler(objects:Map[String,Any]) = new Function[String,Option[String]] {
    import javax.script._;
    val engine = (new ScriptEngineManager).getEngineByName("JavaScript")
    objects.map { case (name,obj) => engine.put(name,obj) }
    def apply(line:String) = {
      line match {
        case ":help" => 
          Some("""Write JS, `enter` to submit. ":quit" or ":exit" to end session.""")
        case (":quit" | ":exit") => 
          throw new ConversationEnded("Goodbye :)")
        case c =>
          try {
            val r = engine.eval(c)
            Option(r).map(_.toString)
          } catch {
            case e => 
              println(e.toString)
              Some("Got error! " + e.getClass.getName)
          }
      }
    }
  }
  
  def jsEndpoint(objects:Map[String,Any], welcome: => String = ("Welcome to rjs4scala! server time: %tT" format new java.util.Date), port:Int = 8093) {
    endpoint(handler = jshandler(objects), port = port, welcome = welcome)
  }
  
  def endpoint(welcome: => String, handler:(String=>Option[String]), port:Int) {
    val serverSocket = new ServerSocket(port)
    while (true) {
      val clientSocket = serverSocket.accept
      val out = new PrintWriter(clientSocket.getOutputStream, true)
      val in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream))
      try {
        out.println(welcome)
        Iterator.continually{in.readLine}.takeWhile(null !=).foreach { l =>
          handler(l.trim).foreach(out.println)
        }
      } catch {
        case ConversationEnded(farewell) => out.println(farewell)
      } finally {
        in.close
        out.close
        clientSocket.close
      }
    }
  }
}

object Mina {
  def name = "afferono"
  def push(s:String) = println("Got: " + s)
  def shutdown = System.exit(0)
  import scala.reflect.BeanProperty
  @BeanProperty var ivar = 1
  val arr = Array(1,2,3)
  def getArr = "Array" + arr.toList.toString.drop(4)
}
