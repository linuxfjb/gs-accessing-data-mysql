#this is manual command line testing spring boot (not sure post works anymore for adding a user)
cd gs-accessing-data-mysql/
cd complete/
./mvnw clean package
cd target/
#run this and fork to its own process
java -jar target/accessing-data-mysql-complete-0.0.1-SNAPSHOT.jar &
#check for the running process
ps -awux | grep java

#test adding a user
curl localhost:8080/demo/add -d name=First -d email=someemail@someprovider.com
#test viewing a user
curl localhost:8080/demo/all
