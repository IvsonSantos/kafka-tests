Para testar use o endopoint:
GET http://localhost:8080/send?message=HelloKafka

Precisa iniciar o kafka docker com:
docker run -p 9092:9092 apache/kafka-native:3.9.0
