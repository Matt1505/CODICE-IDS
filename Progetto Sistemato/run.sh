#!/bin/bash

# 1. Verifica se il file è stato passato come argomento
if [ -z "$1" ] || [ ! -f "$1" ]; then
    echo "Errore: Specifica un percorso di un file .java valido."
    echo "Uso: $0 client/gestioneCredenziali/LoginBoundary.java"
    exit 1
fi

# Definiamo i percorsi di JavaFX, MySQL, Jakarta Mail e Jakarta Activation
FX_PATH="/home/zoppo/Scrivania/IDS/Progetto/libraries/javafx-sdk-21.0.11/lib"
SQL_PATH="/home/zoppo/Scrivania/IDS/Progetto/libraries/mysql/mysql-connector-j-9.7.0.jar"
MAIL_PATH="/home/zoppo/Scrivania/IDS/Progetto/libraries/jakarta.mail-2.0.1.jar"
ACTIVATION_PATH="/home/zoppo/Scrivania/IDS/Progetto/libraries/jakarta.activation-api-2.1.3.jar"

# Pulizia preventiva dei vecchi file compilati nelle cartelle esistenti
echo "Pulizia dei vecchi file compilati (.class)..."
# Trova ed elimina in modo ricorsivo tutti i file .class per evitare problemi di pulizia incompleta
find . -name "*.class" -type f -delete

# 2. Compilazione
echo "Compilazione di tutti i moduli in corso..."

# CORREZIONE: Utilizziamo 'find' per trovare TUTTI i file .java nel progetto, 
# inclusi quelli nei sotto-package come client/gestioneCredenziali/
find . -name "*.java" > sorgenti.txt

javac --module-path "$FX_PATH" --add-modules javafx.controls -cp ".:$SQL_PATH:$MAIL_PATH:$ACTIVATION_PATH" @sorgenti.txt

# Rimuoviamo il file temporaneo della lista sorgenti
rm -f sorgenti.txt

# Controlla se la compilazione ha avuto successo
if [ $? -ne 0 ]; then
    echo "Errore critico durante la compilazione. Interruzione dello script."
    exit 1
fi

# 3. Estrazione del nome della classe con il suo package (es. client.gestioneCredenziali.LoginBoundary)
CLASS_NAME=$(echo "$1" | sed 's/\.java//' | sed 's/\//\./g')

# Rimuove l'eventuale punto iniziale se il percorso passava come ./client/...
CLASS_NAME=$(echo "$CLASS_NAME" | sed 's/^\.\.//' | sed 's/^\.//')

# 4. Esecuzione
echo "Avvio di $CLASS_NAME..."
java --module-path "$FX_PATH" --add-modules javafx.controls -cp ".:$SQL_PATH:$MAIL_PATH:$ACTIVATION_PATH" "$CLASS_NAME"