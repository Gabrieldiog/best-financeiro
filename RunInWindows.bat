@echo off
cd /d "%~dp0"

dir /s /b src\main\java\*.java > sources.txt
javac -cp "lib/*" -d out @sources.txt
del sources.txt

if errorlevel 1 (
    echo ERRO AO COMPILAR
    pause
    exit /b
)

java -cp "out;lib/*" App

pause
