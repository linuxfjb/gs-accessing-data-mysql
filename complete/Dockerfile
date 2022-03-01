#lightweight container with just compiled jar inside
FROM openjdk:8-jdk-alpine

COPY target/accessing-data-mysql-complete-0.0.1-SNAPSHOT.jar app.jar
COPY ../../../../../mydata ./mydata

COPY docker-compose_container.yml docker-compose.yml

ENTRYPOINT ["java","-jar","/app.jar"]
