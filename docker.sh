#!/bin/bash

# Полная установка
# docker pull ubuntu
# docker pull openjdk
# docker pull gradle

docker pull mysql:5.6

docker run --name mysql-standalone4 -e MYSQL_ROOT_PASSWORD=1111 -e MYSQL_DATABASE=mafia -e MYSQL_USER=mafia -e MYSQL_PASSWORD=1111 -d mysql:5.6

docker build . -t mafia-online-1.0

docker start mysql-standalone4

docker run -p 3647:3647 --name mafia-online-1.0 --link mysql-standalone4:mysql -d mafia-online-1.0

docker start mafia-online-1.0

docker logs mafia-online-1.0


# docker stop mysql-standalone4
# docker stop mafia-online-1.0
# docker rm mysql-standalone4
# docker rm mafia-online-1.0
# docker image rm mafia-online-1.0:latest