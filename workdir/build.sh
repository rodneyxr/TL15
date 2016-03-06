#!/bin/sh

ROOT=..
SOURCES=$ROOT/src/core/*
BIN=bin

echo "Cleaning..."
rm -rf $BIN
mkdir -p $BIN

echo "Compiling..."
javac -cp . -d $BIN $SOURCES

echo "Done."
