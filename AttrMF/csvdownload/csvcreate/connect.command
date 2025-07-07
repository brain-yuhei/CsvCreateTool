#!/bin/bash

echo "MySQLのユーザー名を入力してください:"
read name

echo "MySQLのパスワードを入力してください:"
read password


DDL_FILE="create_db.ddl"

# DDL実行
mysql -u "$name" -p"$password" < "$DDL_FILE"
RESULT=$?

# 結果表示
if [ $RESULT -eq 0 ]; then
    echo "DDLファイルの実行に成功しました。"
else
    echo "DDLファイルの実行に失敗しました。"
fi

read -p "Enter キーを押すと終了します。"
