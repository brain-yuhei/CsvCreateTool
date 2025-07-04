@echo off
call mvnw.cmd clean install
call mvnw.cmd spring-boot:run

pause >nul