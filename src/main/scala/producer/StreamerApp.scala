package producer

import akka.actor.ActorSystem
import akka.camel.CamelExtension
import common.Routes

object StreamerApp {
  val system = ActorSystem(Routes.producerSystemName)
  val camel = CamelExtension(system)
  val camelContext = camel.context


  def main(args: Array[String]): Unit = {
    val producers = system.actorOf(Producers.props(), Routes.producersName)

    args.foreach(arg => producers ! NewStreamer(arg))
  }
}
