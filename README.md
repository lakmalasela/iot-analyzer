# IoT Platform Prototype (Spring Boot, Kafka, MongoDB, Docker)

This repository is a **prototype multi-module Maven** project demonstrating a scalable microservice-style architecture for ingesting, processing, and storing IoT device data.

Modules:
- `ingest-service`: REST API to receive IoT device messages and publish them to Kafka.
- `processor-service`: Kafka consumer that persists messages into MongoDB.

Included:
- `docker-compose.yml` to run Zookeeper, Kafka and MongoDB for local testing.
- Dockerfiles for each service.
- Basic code showing message flow end-to-end.

## How it works (overview)
1. Devices POST JSON messages to `ingest-service` (`/api/v1/devices/ingest`).
2. `ingest-service` publishes messages to Kafka topic `iot-messages`.
3. `processor-service` consumes from `iot-messages` and saves records to MongoDB collection `iot_messages`.

## Requirements
- Docker & Docker Compose
- Java 17
- Maven 3.8+

## Build & Run (using Docker Compose)
1. Build the jars:
   ```bash
   mvn -T 1C clean package
   ```
2. Start infrastructure with docker-compose:
   ```bash
   docker compose up -d
   ```
3. Build images and run services (from repo root):
   ```bash
   docker build -f ingest-service/Dockerfile -t ingest-service:local ingest-service
   docker build -f processor-service/Dockerfile -t processor-service:local processor-service
   docker run -e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092 --network iotnet --name ingest ingest-service:local
   docker run -e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092 -e SPRING_DATA_MONGODB_HOST=mongodb --network iotnet --name processor processor-service:local
   ```
   (Simpler: adapt the docker-compose to build + run; examples provided.)

## Testing
Send a POST to ingest-service:
```bash
curl -X POST http://localhost:8081/api/v1/devices/ingest \
 -H 'Content-Type: application/json' \
 -d '{"deviceId":"dev-123","timestamp":1699132800000,"temperature":26.5,"humidity":70.2}'
```

Then check MongoDB `iotdb` database `iot_messages` collection for saved documents.

## Notes & Next steps
- This is a minimal prototype. Production-ready systems require:
  - Schema validation, authentication/authorization
  - Partitioning strategy for Kafka topics and consumer groups for scale
  - Proper error handling and DLQ (dead-letter queues)
  - Monitoring (Prometheus/Grafana), tracing (OpenTelemetry)
  - Stateful stream processing (Kafka Streams or Flink) for analytics
  - Kubernetes deployment, autoscaling, secrets management

