#!/bin/sh

BIN=bin

if [ ! -d "$BIN" ]; then
    echo "Error: You must run 'build.sh' first!"
    exit 
fi

java -cp $BIN core.Main $@
