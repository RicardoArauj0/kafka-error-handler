# kafka-error-handler

### Description
<p>This project is an example on how we can handle exception when processing a kafka message using Spring Boot.
I'm using a ObjectMapper from Jackson to convert a string JSON to an object. This ObjectMapper don't have any
additional configuration, and it will throw an UnrecognizedPropertyException when we send a JSON with any additional 
field different from the Person model in the project.</p>

<p>On KafkaConfig class there are a configuration to retry to process the message
and it will post the message on a DLT if the retries doesn't succeed.</p>


### Set up

Run 'docker-compose up' on the project root to set up a Kafka Cluster

Set up the producer:
- kafka-console-producer --bootstrap-server localhost:9092 --topic topic-test

Set up the consumer for the DLT:
- kafka-console-consumer --bootstrap-server localhost:9092 --topic topic-test.DLT

Example of a message that will not throw any exception:
- {"name":"Luke", "surname":"Skywalker"}

Example of a message that will throw a 'UnrecognizedPropertyException':
- {"name":"Luke", "surname":"Skywalker", "job":"Jedi Master"}
