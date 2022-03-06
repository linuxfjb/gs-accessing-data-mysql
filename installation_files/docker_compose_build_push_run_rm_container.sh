sudo docker login -u linuxfjb

sudo docker build -t linuxfjb/gs-spring-boot-docker .
sudo docker push linuxfjb/gs-spring-boot-docker

sudo docker-compose -f docker-compose.yml up -d
./mvnw test

sudo docker-compose -f docker-compose_container.yml up -d

sudo docker run --network complete_mynetwork -p 8081:8081 -e spring.datasource.url="jdbc:mysql://db:3306/db_example" -d linuxfjb/gs-spring-boot-docker

#do not remove saved remove image
#sudo docker rm linuxfjb/gs-spring-boot-docker

#removed unneeded local image and containers
sudo docker image prune -f
sudo docker container prune -f

