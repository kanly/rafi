# rafi
Streams a log over the network using akka remoting.

It is made of two subproject the producer and the consumer. 
The source is built using sbt with the assembly plugin

open sbt ant type following commands

```
project producer
assembly
project consumer
assembly
```
Producer jar will be in _producer/target/scala-2.11/rafi-producer.jar_

Consumer jar will be in _consumer/target/scala-2.11/rafi-consumer.jar_

To start producer jar use
```
java -jar rafi-producer.jar <path_to_log_1> [... <path_to_logfile_n>] 
```
by default it will bind on **0.0.0.0:9599** to change this defaults use _bindAddress_ and _bindPort_ system variables

To start consumer jar use
```
java -jar rafi-consumer.jar <path_to_local_log_dir> <producer_ip> <producer_port>
```
by default it will bind on **0.0.0.0:9598** to change this defaults use _bindAddress_ and _bindPort_ system variables
