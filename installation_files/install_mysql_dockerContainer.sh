cd /home/linuxfjb/gs-accessing-data-mysql/complete
sudo docker pull mysql:5.6
mkdir ./mydata
echo "created mysql data storage directory to work with docker container creation."
echo "setting up a container named mysql_db with db:db_example and springuser:ThePassword"
sudo docker run --name=mysql_db -e MYSQL_ROOT_PASSWORD=centos -e MYSQL_DATABASE=db_example -e MYSQL_USER=springuser -e MYSQL_PASSWORD=ThePassword -v ./mydata:/var/lib/mysql -p 3306:3306 -d mysql:5.6 
sudo docker logs mysql_db

#for manually checking the db on host machine
echo "install mysql local client on host machine"
sudo apt install mysql-client-core-5.7

#manually test db connection (have to specify tcp and use a numerical ip
#mysql -h 127.0.0.1 -P 3306 -u root -p --protocol=tcp
#mysql -h 127.0.0.1 -P 3306 -u springuser -p --protocol=tcp
