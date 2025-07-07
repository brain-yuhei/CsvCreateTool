#!/bin/bash
cd "$(dirname "$0")"

./mvnw clean install
./mvnw spring-boot:run

read -p "Enter キーを押して終了します。"
