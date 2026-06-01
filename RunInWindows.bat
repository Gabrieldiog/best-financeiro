@echo off
cd /d "%~dp0"

javac -cp "lib/*" -d out src/main/java/utils/Conexao.java src/main/java/model/Usuario.java src/main/java/DAO/UsuarioDAO.java src/main/java/App.java

if errorlevel 1 (
    echo ERRO AO COMPILAR
    pause
    exit /b
)

java -cp "out;lib/*" App

pause