package consumer

import java.nio.file.Path

import akka.actor.{ActorLogging, Actor}

import scala.collection.mutable

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
