How to run: <br/>
mvn clean package <br/>
*you can skip this stage as I have uploaded target to github too <br/>
java -jar fat-jar/vertx-1.0-SNAPSHOT-fat.jar <br/>

go to: <br/>
List accounts (initial datastore): <br/>
http://localhost:8080/api/accounts <br/>
same could be achieved using curl: <br/>
curl -X GET http://localhost:8080/api/accounts <br/>
transfer between accounts: <br/>
curl -X PUT http://localhost:8080/api/accounts/transfer --data "{ \"emailFrom\": \"email1@gmail.com\", \"emailTo\": \"email2@gmail.com\", \"amount\": \"25\"  }" <br/>


