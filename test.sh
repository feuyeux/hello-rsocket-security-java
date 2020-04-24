#!/usr/bin/env bash
echo "hello-forget"
curl http://localhost:8989/api/hello-forget
echo
echo "hello-response"
curl http://localhost:8989/api/hello/1
echo
echo "hello-stream"
curl http://localhost:8989/api/hello-stream
echo
echo "hello-channel"
curl http://localhost:8989/api/hello-channel