#!/bin/bash

PROJECT_DIR=$(dirname "$0")
echo $PROJECT_DIR
cd "$PROJECT_DIR" || exit 1
cd ..

echo "...................Cleaning and building the project..................."
mvn clean install || { echo "Build failed! Exiting."; exit 1; }

sleep 10

echo "...................Running Single Process Main class..................."
mvn exec:java -Dexec.mainClass="org.akshya.singleProcess.Main" &
echo "...................Single Process Main class Stopped..................."

sleep 2

echo "...................Running Separate PID Player..................."
echo "...................Starting ServerPlayer..................."
mvn exec:java -Dexec.mainClass="org.akshya.sockets.ServerPlayer" &

sleep 2

echo "...................Starting ClientPlayer..................."
mvn exec:java -Dexec.mainClass="org.akshya.sockets.ClientPlayer"

echo "...................All processes executed successfully!..................."
