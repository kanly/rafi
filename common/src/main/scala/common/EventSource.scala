/*
 Rafi streams a log file to a remote machine
     Copyright (C) 2015  Paolo Mazzoncini

     This program is free software: you can redistribute it and/or modify
     it under the terms of the GNU Affero General Public License as
     published by the Free Software Foundation, either version 3 of the
     License, or (at your option) any later version.

     This program is distributed in the hope that it will be useful,
     but WITHOUT ANY WARRANTY; without even the implied warranty of
     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     GNU Affero General Public License for more details.

     You should have received a copy of the GNU Affero General Public License
     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
