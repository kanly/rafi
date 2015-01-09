package consumer

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
