@echo off
setlocal

rem MySQL実行ファイルのパス
set "MYSQL_PATH=C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"

rem ユーザー入力
set /p USER=MySQLのユーザー名を入力してください:
set /p PASSWORD=MySQLのパスワードを入力してください:

rem DDLファイル名
set "DDL_FILE=connect.ddl"

rem DDL実行
"%MYSQL_PATH%" -u %USER% -p%PASSWORD% < "%DDL_FILE%"

rem 実行結果チェック
if %errorlevel% equ 0 (
    echo DDLファイルの実行に成功しました。
) else (
    echo DDLファイルの実行に失敗しました。
)

pause
endlocal
