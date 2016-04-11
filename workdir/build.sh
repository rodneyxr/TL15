#!/bin/sh

ROOT=..
SOURCES=$ROOT/src/core/*.java 
BIN=bin

echo "Cleaning..."
rm -rf $BIN
mkdir -p $BIN

echo "Compiling..."
javac -cp . -d $BIN $SOURCES $ROOT/src/core/*/**.java

echo "Done."
