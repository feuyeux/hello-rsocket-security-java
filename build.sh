#!/usr/bin/env bash
BUILD_DIR="$(cd "$(dirname "$0")" && pwd)"
cd $BUILD_DIR/common
echo "build common jar"
mvn clean install -DskipTests
echo
cd $BUILD_DIR/responder
echo "build responder jar"
mvn clean install -DskipTests
echo
cd $BUILD_DIR/requester
echo "build requester jar"
mvn clean install -DskipTests
echo