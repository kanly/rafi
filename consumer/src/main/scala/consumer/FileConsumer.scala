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


import akka.actor.{Props, ActorLogging, Actor}
import common.{LogLine, SubscribeAll, Routes, UnsubscribeAll}

import scala.collection.mutable
import scalax.io._
import scalax.file.Path

class FileConsumer(writeDir: String, remoteAddress: String, remotePort: String) extends Actor with ActorLogging {
  implicit val codec = scalax.io.Codec.UTF8

  checkOutDir()

  private val files: mutable.Map[String, Seekable] = mutable.Map()

  def receive = {
    case LogLine(line, filename) => files.getOrElseUpdate(filename, createSeekable(s"$writeDir/$filename")).append(s"$line \n")
  }

  def createSeekable(filePath: String) = {
    val outFile = Path.fromString(filePath)
    if (!outFile.exists && !outFile.doCreateFile()) throw new Error(s"cannot create file $filePath")
    if (!outFile.canWrite) throw new Error(s"cannot write to file $filePath")
    Resource.fromFile(outFile.jfile)
  }

  override def preStart() {
    val remoteActor = context.actorSelection(s"akka.tcp://${Routes.producerSystemName}@$remoteAddress:$remotePort/user/${Routes.producersName}")
    remoteActor ! SubscribeAll
    log.info("Subscribe message sent")
  }

  override def postStop() {
    val remoteActor = context.actorSelection(s"akka.tcp://${Routes.producerSystemName}@$remoteAddress:$remotePort/user/${Routes.producersName}")
    remoteActor ! UnsubscribeAll
    log.info("Unsubscribe message sent")
  }

  private def checkOutDir() {
    val outDir = Path.fromString(writeDir)
    if (outDir.exists) {
      if (outDir.isDirectory) {
        if (!outDir.canWrite) throw new Error(s"Outdir $writeDir is not writable")
      }
      else {
        throw new Error(s"Outdir $writeDir is not a directory")
      }
    } else {
      outDir.doCreateParents()
      if (!outDir.doCreateDirectory()) throw new Error(s"Error while creating outDir $writeDir")
    }
  }
}

object FileConsumer {
  def props(writeDir: String, remoteAddress: String, remotePort: String) = Props(new FileConsumer(writeDir, remoteAddress, remotePort))
}
