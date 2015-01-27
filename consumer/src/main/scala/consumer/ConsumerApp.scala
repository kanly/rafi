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

package consumer

import akka.actor.ActorSystem
import org.slf4j.LoggerFactory

object ConsumerApp {
  val system = ActorSystem("consumer")
  val log = LoggerFactory.getLogger("MAIN")


  def main(args: Array[String]): Unit = {
    val localDir = args(0)
    val remoteIp = args(1)
    val remotePort = args(2)

    log.info(s"localDir $localDir, remoteIp $remoteIp, remotePort $remotePort")

    system.actorOf(FileConsumer.props(localDir, remoteIp, remotePort))
  }
}
