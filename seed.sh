#!/bin/bash
cd "$(dirname "$0")"

rm -f financeiro.db
javac -cp "lib/*" -d out $(find src/main/java -name "*.java") || { echo "ERRO AO COMPILAR"; exit 1; }

printf '1\nteste\nTeste$1\nTeste$1\n2\nteste\nTeste$1\n1\n1\n3500\n\n1\nSalario do mes\n01/06/2026\n1\n1\n800\n\n3\nProjeto freela\n03/06/2026\n1\n2\n1200\n\n3\nAluguel\n01/06/2026\n1\n2\n200\n\n7\nConta de luz\n01/06/2026\n1\n2\n20\n22\n1\nAlmoco no trabalho\n02/06/2026\n1\n2\n15\n20\n2\nCorridas de app\n03/06/2026\n1\n2\n8\n30\n1\nCafezinho\n04/06/2026\n1\n2\n100\n\n4\nAcademia\n02/06/2026\n1\n2\n50\n\n9\nAssinaturas\nNetflix\n04/06/2026\n10\n3\n' | java -cp "out:lib/*" App > /dev/null

echo "=========================================="
echo " Banco populado com dados de teste!"
echo " Usuario: teste"
echo " Senha:   Teste\$1"
echo "=========================================="
echo "Agora rode ./run.sh e faca login pra testar."
