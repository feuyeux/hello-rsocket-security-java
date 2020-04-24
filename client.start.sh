#!/usr/bin/env bash
BUILD_DIR="$(cd "$(dirname "$0")" && pwd)"
cd $BUILD_DIR/requester
mvn spring-boot:run