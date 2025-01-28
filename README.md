Para testar use o endopoint:
GET http://localhost:8080/send?message=HelloKafka

Precisa iniciar o kafka docker com:
docker run -p 9092:9092 apache/kafka-native:3.9.0


RUN KAFKA LOCALLY:
- on /kafka-mac
- ````
  ./bin/kafka-storage.sh random-uuid
  ./bin/kafka-storage.sh format -t <<uuid>> -c config/kraft/server.properties
  ./bin/kafka-server-start.sh config/kraft/server.propertiesç
``    
  
  

CRIAR TOPICO
- ````
  ./kafka-topics.sh --create --topic topic1 --partitions 3 --replication-factor 1 --bootstrap-server localhost:9092,localhost:9094
``

LIST TOPICOS
- ````
  ./kafka-topics.sh --list --bootstrap-server localhost:9092
      ou
  ./kafka-topics.sh --describe --bootstrap-server localhost:9092
``