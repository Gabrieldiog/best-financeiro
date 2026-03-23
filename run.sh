#!/bin/bash
cd "$(dirname "$0")"
javac -cp "lib/*" -d out src/main/java/utils/Conexao.java src/main/java/model/Usuario.java src/main/java/DAO/UsuarioDAO.java src/main/java/App.java && java -cp "out:lib/*" App
