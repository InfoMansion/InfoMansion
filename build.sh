#! /bin/sh
docker rm spring
docker rm react
docker rm mariadb
docker rm redis

cd mariadb

echo "[BUILD] MariaDB"
docker build --tag mariadb:0.0.1 .
# docker run --name mariadb -d -p 3309:3306 mariadb:0.0.1 --restart=always

echo "[RUN] MariaDB"

docker run \
    --name mariadb \
    -d \
    -p 3309:3306 \
    --restart=always \
    -e MYSQL_ROOT_PASSWORD=root \
    -v mariadb-volume:/var/lib/mysql \
    mariadb:0.0.1

cd ../redis

echo "[BUILD] Redis"
docker build --tag redis:0.0.1 .
echo "[RUN] Redis"
docker run --name redis -d -p 6380:6379 -v redis-volume:/data redis:0.0.1

cd ../frontend

echo "[BUILD] React"
docker build --tag react:0.0.2 .

echo "[RUN] React"
docker run --name react -d -p 3000:3000 -e CI=true react:0.0.2

cd ../backend
# ./gradlew clean bootBuildImage -Pprofile=dev --platform linux/amd64

echo "[BUILD] Spring"
./gradlew clean build
docker build --tag spring:0.0.1 .
echo "[RUN] Spring"
docker run --name spring -d -p 8080:8080 spring:0.0.1

