#restarting the server for testing
kill $(ps auwx | grep java | grep -v grep | sed -e "s/\\s\+/ /g" | cut -d' ' -f2)

cd /home/linuxfjb/gs-accessing-data-mysql/complete/
./mvnw -DskipTests=true package
java -jar /home/linuxfjb/gs-accessing-data-mysql/complete/target/accessing-data-mysql-complete-0.0.1-SNAPSHOT.jar &
