#! /bin/sh
docker rm spring
docker rm react

cd frontend
docker build --tag react:0.0.1 .

cd ../backend
docker build --tag spring:0.0.1 .
docker run --name spring -d -p 8080:8080 spring:0.0.1

cd ../frontend
docker run --name react -d -p 3000:3000 react:0.0.1