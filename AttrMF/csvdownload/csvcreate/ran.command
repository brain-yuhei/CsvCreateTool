#!/bin/bash

./mvnw clean install
./mvnw spring-boot:run

read -p "Enter キーを押して終了します。"