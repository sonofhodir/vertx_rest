How to run:
mvn clean package
*you can skip this stage as I have uploaded target to github too
java -jar fat-jar/vertx-1.0-SNAPSHOT-fat.jar

go to:
List accounts (initial datastore):
http://localhost:8080/api/accounts
same could be achieved using curl:
curl -X GET http://localhost:8080/api/accounts
transfer between accounts:
curl -X PUT http://localhost:8080/api/accounts/transfer --data "{ \"emailFrom\": \"email1@gmail.com\", \"emailTo\": \"email2@gmail.com\", \"amount\": \"25\"  }"


__