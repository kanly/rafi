/*******************************************************************************
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
 ******************************************************************************/

package producer

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.camel.{CamelMessage, Consumer}
import common.ProductionEventSource.{RegisterListener, UnregisterListener}
import common._

import scalax.file.Path

class Streamer(fileUri: String) extends Consumer with ActorLogging {
  this: EventSource =>

  override def endpointUri: String = s"stream:file?fileName=$fileUri&scanStream=true"
  private val name = Path.fromString(fileUri).name

  override def receive = eventSourceReceiver orElse {
    case msg: CamelMessage => sendEvent(LogLine(msg.getBodyAs(classOf[String], StreamerApp.camelContext), name))
    case a => log.warning(s"unknown message received by Streamer actor: $a")
  }
}

object Streamer {
  def props(filePath: String) = Props(new Streamer(filePath) with ProductionEventSource)
}

class Producers extends Actor with ActorLogging {
  private var streamers: List[ActorRef] = Nil

  override def receive = {
    case NewStreamer(path) => streamers = context.actorOf(Streamer.props(path)) :: streamers
    case SubscribeAll => streamers.foreach { actor => actor ! RegisterListener(sender())}
    case UnsubscribeAll => streamers.foreach { actor => actor ! UnregisterListener(sender())}
  }
}

object Producers {
  def props() = Props(new Producers)
}

case class NewStreamer(filePath: String)
