package common

import akka.actor.{Actor, ActorRef}
import common.ProductionEventSource.{RegisterListener, UnregisterListener}

object ProductionEventSource {

  case class RegisterListener(listener: ActorRef)

  case class UnregisterListener(listener: ActorRef)

}

trait EventSource {
  def sendEvent[T](event: T)

  def eventSourceReceiver: Actor.Receive
}


trait ProductionEventSource extends EventSource {
  this: Actor =>

  var listeners = Vector.empty[ActorRef]

  def sendEvent[T](event: T) {
    listeners.foreach {
      _ ! event
    }
  }

  def eventSourceReceiver: Receive = {
    case RegisterListener(listener) => listeners = listeners :+ listener
    case UnregisterListener(listener) => listeners = listeners filter {
      _ != listener
    }
  }
}
