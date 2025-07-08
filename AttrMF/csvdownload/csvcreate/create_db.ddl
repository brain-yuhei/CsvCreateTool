CREATE DATABASE IF NOT EXISTS csv_tool_db;

CREATE USER IF NOT EXISTS 'user'@'localhost' IDENTIFIED BY 'userpassword';

GRANT ALL PRIVILEGES ON csv_tool_db.* TO 'user'@'localhost';

FLUSH PRIVILEGES;