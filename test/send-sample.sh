#!/usr/bin/env bash
# send a sample message to ingest-service (change host if needed)
curl -X POST http://localhost:8081/api/v1/devices/ingest \
 -H 'Content-Type: application/json' \
 -d '{"deviceId":"device-001","timestamp":'$(date +%s)'000,"temperature":23.5,"humidity":55.3}'
