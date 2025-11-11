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
1. Devices POST JSON messages to `ingest-service` (`/api/v1/devices/ingest`) on port **8081**.
2. `ingest-service` publishes messages to Kafka topic `iot-messages`.
3. `processor-service` (port **8082**) consumes from `iot-messages` and saves records to MongoDB collection `iot_messages`.
4. MongoDB runs on port **27018** (instead of default 27017).
5. Kafka is accessible at `localhost:29092` for external connections.

## Requirements
- Docker & Docker Compose
- Java 17
- Maven 3.8+

## Build & Run

### Option 1: Local Development (Without Docker)
1. Start infrastructure with docker-compose (Kafka, Zookeeper, MongoDB):
   ```bash
   docker compose up -d
   ```
2. Build the project:
   ```bash
   mvn clean package
   ```
3. Run the services:
   ```bash
   # Terminal 1: Run ingest-service
   cd ingest-service
   mvn spring-boot:run
   
   # Terminal 2: Run processor-service
   cd processor-service
   mvn spring-boot:run
   ```

### Option 2: Full Docker Deployment
1. Build the jars:
   ```bash
   mvn clean package
   ```
2. Build Docker images:
   ```bash
   docker build -f ingest-service/Dockerfile -t ingest-service:local ingest-service
   docker build -f processor-service/Dockerfile -t processor-service:local processor-service
   ```
3. Start all services:
   ```bash
   docker compose up -d
   ```

## Testing

### Send Test Data
Send a POST to ingest-service:
```bash
curl -X POST http://localhost:8081/api/v1/devices/ingest \
 -H 'Content-Type: application/json' \
 -d '{"deviceId":"dev-123","timestamp":1699132800000,"temperature":26.5,"humidity":70.2}'
```

Expected response:
```
accepted
```

### Verify Data in MongoDB
Connect to MongoDB (port **27018**):
```bash
mongosh --port 27018
```

Check the data:
```javascript
use iot-analyzer
db.iot_messages.find().pretty()
```

Note: The processor-service is configured to use MongoDB Atlas cloud database by default. To use local MongoDB, update `processor-service/src/main/resources/application.properties`:
```properties
# Comment out the cloud URI:
# spring.data.mongodb.uri=mongodb+srv://...

# Add local configuration:
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27018
spring.data.mongodb.database=iot-analyzer
```

## Configuration Details

### Ingest Service (Port 8081)
- Kafka bootstrap server: `localhost:29092`
- Kafka topic: `iot-messages`
- Serializer: JSON (Spring Kafka JsonSerializer)

### Processor Service (Port 8082)
- Kafka bootstrap server: `localhost:29092`
- Consumer group: `processor-group`
- Deserializer: ErrorHandlingDeserializer with JsonDeserializer delegate
- MongoDB: MongoDB Atlas cloud (default) or localhost:27018
- Database: `iot-analyzer`
- Collection: `iot_messages`

### Docker Compose Services
- **Zookeeper**: Port 22181 (external) / 2181 (internal)
- **Kafka**: Port 29092 (external) / 9092 (internal)
- **MongoDB**: Port 27018



