#!/bin/sh

cd ..
mkdir -p bin
javac -cp . -d bin src/core/*
