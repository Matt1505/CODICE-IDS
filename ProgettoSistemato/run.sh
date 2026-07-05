#!/bin/bash

# 1. Verifica se il file è stato passato come argomento
if [ -z "$1" ] || [ ! -f "$1" ]; then
    echo "Errore: Specifica un percorso di un file .java valido."
    echo "Uso: $0 src/MacroGestioneCredenziali/LoginBoundary.java"
    exit 1
fi

# Librerie
FX_PATH="./src/libraries/javafx-sdk-21.0.11/lib"
SQL_PATH="./src/libraries/mysql/mysql-connector-j-9.7.0.jar"
MAIL_PATH="./src/libraries/jakarta.mail-2.0.1.jar"
ACTIVATION_PATH="./src/libraries/jakarta.activation-api-2.1.3.jar"

# Dropbox + Jackson
DROPBOX_PATH="./src/libraries/dropbox-core-sdk-8.0.1.jar"
JACKSON_ANNOTATIONS_PATH="./src/libraries/jackson-annotations-3.0-rc5.jar"
JACKSON_CORE_PATH="./src/libraries/jackson-core-2.22.0.jar"
JACKSON_DATABIND_PATH="./src/libraries/jackson-databind-2.22.0.jar"

# Classpath completo
CP=".:$SQL_PATH:$MAIL_PATH:$ACTIVATION_PATH:$DROPBOX_PATH:$JACKSON_ANNOTATIONS_PATH:$JACKSON_CORE_PATH:$JACKSON_DATABIND_PATH"

echo "Pulizia dei vecchi file compilati (.class)..."
find . -name "*.class" -type f -delete

echo "Compilazione di tutti i moduli in corso..."
find . -name "*.java" > sorgenti.txt

javac --module-path "$FX_PATH" \
      --add-modules javafx.controls,javafx.media \
      -cp "$CP" \
      @sorgenti.txt

COMPILATION_RESULT=$?

rm -f sorgenti.txt

if [ $COMPILATION_RESULT -ne 0 ]; then
    echo "Errore critico durante la compilazione. Interruzione dello script."
    exit 1
fi

CLASS_NAME=$(echo "$1" | sed 's/\.java//' | sed 's/\//\./g')
CLASS_NAME=$(echo "$CLASS_NAME" | sed 's/^\.\.//' | sed 's/^\.//')

echo "Avvio di $CLASS_NAME..."

java --enable-native-access=javafx.graphics,javafx.media \
     --module-path "$FX_PATH" \
     --add-modules javafx.controls,javafx.media \
     -cp "$CP" \
     "$CLASS_NAME"