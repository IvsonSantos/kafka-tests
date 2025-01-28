Para testar use o endpoint:
POST http://localhost:8080/upload

form-data
key: file
value: <<arquivo.txt>>

O arquivo será salvo em /tmp/arquivo.txt
```
Docker:

Run the mvn clean install

docker build . -t kafka

docker run -it -p 8080:8080 kafka
