#!/bin/bash

# 1. Verifica se il file è stato passato come argomento
if [ -z "$1" ] || [ ! -f "$1" ]; then
    echo "Errore: Specifica un percorso di un file .java valido."
    echo "Uso: $0 client/gestioneCredenziali/LoginBoundary.java"
    exit 1
fi

# Cartella principale del progetto, cioè dove si trova run.sh
BASE_DIR="$(cd "$(dirname "$0")" && pwd)"

# JavaFX: prima cerca dentro il progetto, poi nel sistema
PROJECT_FX_PATH="$BASE_DIR/libraries/javafx-sdk-21.0.11/lib"
SYSTEM_FX_PATH="/usr/share/openjfx/lib"

if [ -f "$PROJECT_FX_PATH/javafx.controls.jar" ]; then
    FX_PATH="$PROJECT_FX_PATH"
elif [ -f "$SYSTEM_FX_PATH/javafx.controls.jar" ]; then
    FX_PATH="$SYSTEM_FX_PATH"
else
    echo "Errore: JavaFX non trovato."
    echo "Soluzione 1: installa JavaFX con:"
    echo "sudo apt update && sudo apt install openjfx"
    echo ""
    echo "Soluzione 2: controlla che esista questo file:"
    echo "$PROJECT_FX_PATH/javafx.controls.jar"
    exit 1
fi

# Librerie esterne, prese dalla cartella del progetto
SQL_PATH="$BASE_DIR/libraries/mysql/mysql-connector-j-9.7.0.jar"
MAIL_PATH="$BASE_DIR/libraries/jakarta.mail-2.0.1.jar"
ACTIVATION_PATH="$BASE_DIR/libraries/jakarta.activation-api-2.1.3.jar"

# Classpath
CP=".:$SQL_PATH:$MAIL_PATH:$ACTIVATION_PATH"

# Pulizia preventiva dei vecchi file compilati
echo "Pulizia dei vecchi file compilati (.class)..."
find . -name "*.class" -type f -delete

# 2. Compilazione
echo "Compilazione di tutti i moduli in corso..."
echo "Uso JavaFX da: $FX_PATH"

find . -name "*.java" > sorgenti.txt

javac --module-path "$FX_PATH" \
      --add-modules javafx.controls \
      -cp "$CP" \
      @sorgenti.txt

# Salviamo il risultato della compilazione prima di cancellare sorgenti.txt
COMPILATION_RESULT=$?

rm -f sorgenti.txt

# Controlla se la compilazione ha avuto successo
if [ $COMPILATION_RESULT -ne 0 ]; then
    echo "Errore critico durante la compilazione. Interruzione dello script."
    exit 1
fi

# 3. Estrazione del nome della classe con il suo package
# esempio: ./client/gestioneCredenziali/LoginBoundary.java
# diventa: client.gestioneCredenziali.LoginBoundary
CLASS_NAME=$(echo "$1" | sed 's|^\./||' | sed 's|\.java$||' | sed 's|/|.|g')

# 4. Esecuzione
echo "Avvio di $CLASS_NAME..."

java --module-path "$FX_PATH" \
     --add-modules javafx.controls \
     -cp "$CP" \
     "$CLASS_NAME"