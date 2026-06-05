#!/bin/bash
cd "$(dirname "$0")"
javac -cp "lib/*" -d out $(find src/main/java -name "*.java") && java -cp "out:lib/*" App
