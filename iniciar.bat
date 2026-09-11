@echo off
cd /d "%~dp0"
chcp 65001 > nul
title VAULT - Seu Cofre

if not exist "target\classes\com\sxiii\Main.class" (
    echo Compilando o projeto...
    mvn -q compile dependency:copy-dependencies
)

if not exist "target\dependency\" (
    echo Copiando dependencias...
    mvn -q dependency:copy-dependencies
)

java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "target\classes;target\dependency\*" com.sxiii.Main

echo.
pause
