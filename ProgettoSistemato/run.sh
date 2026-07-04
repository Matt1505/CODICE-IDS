#!/bin/bash

# 1. Verifica se il file è stato passato come argomento
if [ -z "$1" ] || [ ! -f "$1" ]; then
    echo "Errore: Specifica un percorso di un file .java valido."
    echo "Uso: $0 src/gestioneCredenziali/LoginBoundary.java"
    exit 1
fi

# Definiamo i percorsi delle librerie (Aggiornati in base alla nuova cartella 'src/libraries')
FX_PATH="./src/libraries/javafx-sdk-21.0.11/lib"
SQL_PATH="./src/libraries/mysql/mysql-connector-j-9.7.0.jar"
MAIL_PATH="./src/libraries/jakarta.mail-2.0.1.jar"
ACTIVATION_PATH="./src/libraries/jakarta.activation-api-2.1.3.jar"

# Pulizia preventiva dei vecchi file compilati nelle cartelle esistenti
echo "Pulizia dei vecchi file compilati (.class)..."
find . -name "*.class" -type f -delete

# 2. Compilazione
echo "Compilazione di tutti i moduli in corso..."

# Trova TUTTI i file .java nel progetto
find . -name "*.java" > sorgenti.txt

javac --module-path "$FX_PATH" --add-modules javafx.controls,javafx.media -cp ".:$SQL_PATH:$MAIL_PATH:$ACTIVATION_PATH" @sorgenti.txt

COMPILATION_RESULT=$?

rm -f sorgenti.txt

if [ $COMPILATION_RESULT -ne 0 ]; then
    echo "Errore critico durante la compilazione. Interruzione dello script."
    exit 1
fi

# 3. Estrazione del nome della classe con il suo package (es. src.gestioneCredenziali.LoginBoundary)
# Converte i separatori di percorso "/" in punti "." per Java
CLASS_NAME=$(echo "$1" | sed 's/\.java//' | sed 's/\//\./g')

# Rimuove eventuali punti residui all'inizio (es. se passi ./src/... o ../src/...)
CLASS_NAME=$(echo "$CLASS_NAME" | sed 's/^\.\.//' | sed 's/^\.//')

# 4. Esecuzione
echo "Avvio di $CLASS_NAME..."
java --module-path "$FX_PATH" --add-modules javafx.controls,javafx.media -cp ".:$SQL_PATH:$MAIL_PATH:$ACTIVATION_PATH" "$CLASS_NAME"