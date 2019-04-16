package kafkaTrain

/**
  * Start ZooKeeper:
  * bin/zkServer.sh start
  * bin/zkCli.sh
  * bin/zookeeper-server-start.sh config/zookeeper.properties

  *
  * Start Kafka broker:
  * $ bin/kafka-server-start.sh config/server.properties
  * $ bin/kafka-server-stop.sh config/server.properties
  *
  * Create topics
  * bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic Hello-Kafka
  *
  *  Listing topics
    bin/kafka-topics.sh --list --zookeeper localhost:2181

    bin/kafka-topics.sh --describe --zookeeper localhost:2181

    bin/kafka-console-producer.sh --broker-list localhost:9092 --topic HelloWorld

    bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic HelloWorld --from-beginning --partition 1

    bin/kafka-topics.sh --delete --topic  Hello-Kafka --zookeeper localhost:2181
  */
class HelloWorld {

}
