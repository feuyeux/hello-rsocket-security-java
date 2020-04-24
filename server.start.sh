#!/usr/bin/env bash
BUILD_DIR="$(cd "$(dirname "$0")" && pwd)"
cd $BUILD_DIR/responder
mvn spring-boot:run