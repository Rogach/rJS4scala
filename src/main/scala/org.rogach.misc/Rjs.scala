package org.rogach.misc

import java.net.InetSocketAddress
import java.nio.charset.Charset
import java.net._
import java.io._

object Rjs {

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
  
  def defwelcome = ("Welcome to rjs4scala! server time: %tT" format new java.util.Date)
  
  def jsEndpoint(objects:Map[String,Any] = Map(), welcome: => String = defwelcome, port:Int = 8093) {
    endpoint(handler = jshandler(objects), port = port, welcome = welcome)
  }
  
  def endpoint(welcome: => String = defwelcome, handler:(String=>Option[String]) = jshandler(Map()), port:Int = 8093) = {
    new Thread {
      override def run() {
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
    }.start
  }
}

