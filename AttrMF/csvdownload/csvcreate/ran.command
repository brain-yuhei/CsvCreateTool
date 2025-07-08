#!/bin/bash
cd "$(dirname "$0")"

# Java環境のパスを手動で設定（ご自身のJDKに合わせて変更してください）
export JAVA_HOME=$(/usr/libexec/java_home)
export PATH="$JAVA_HOME/bin:$PATH"

# 実行権限がない場合のために（初回のみ）
chmod +x ./mvnw

# クリーンビルドと起動
./mvnw clean install
./mvnw spring-boot:run

echo "Enter キーを押して終了します。"
read

