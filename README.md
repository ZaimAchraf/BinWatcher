# 📦 Chapter 4: Bin Filling Management with Asynchronous Communication via Kafka

## 🌟 Chapter Overview
This chapter introduces an asynchronous mechanism for managing bin fill levels using **Apache Kafka** as a message broker.  
The goal is to simulate real sensors sending fill level updates, have these updates consumed by the bin-service, update the database, and trigger alerts if a bin exceeds its configured threshold.

---

## ⚙️ Architecture & Components

### 1. Sensor-Service
- Simulates a real-world IoT sensor.
- Periodically retrieves the list of bins from `bin-service` via **OpenFeign**.
- Randomly generates fill level updates for bins.
- Publishes `FillMessage` events to the `bin-fill` Kafka topic.

**Key classes:**
- `BinClient` → Feign client to call `bin-service`
- `KafkaProducerService` → Sends messages to Kafka
- `ScheduledService` → Periodically fetches bins & sends mock fill levels

---

### 2. Bin-Service
- Consumes `FillMessage` events from Kafka.
- Updates the fill level in the MongoDB database.
- Generates a `FillAlert` if the bin’s fill level exceeds its alert threshold.
- Publishes the `FillAlert` to another Kafka topic for other services to consume.

**Key classes:**
- `KafkaConfig` → Configures Kafka producer & consumer
- `BinFillConsumerService` → Listens to `bin-fill` topic, updates DB, and triggers alerts
- `BinService` → Core business logic for updating fill levels and generating alerts

---

### 3. API Module
- New shared module containing common models (`FillMessage`, `FillAlert`, `Bin`, etc.).
- Ensures consistent serialization & deserialization between producer and consumer.
- Centralizes reusable code for cross-service communication.

---

## 🗄️ Database & Message Broker
Both MongoDB and Kafka run inside **Docker containers** using `docker-compose`:

```yaml
services:
  mongodb:
    image: mongo:latest
    container_name: BW_DB
    ports:
      - "29017:27017"
    environment:
      MONGO_INITDB_ROOT_USERNAME: admin
      MONGO_INITDB_ROOT_PASSWORD: admin
    volumes:
      - BW_DB:/data/db

  zookeeper:
    image: 'bitnami/zookeeper:latest'
    container_name: zookeeper
    environment:
      - ALLOW_ANONYMOUS_LOGIN=yes
    ports:
      - "2181:2181"

  kafka:
    image: 'bitnami/kafka:latest'
    container_name: kafka
    environment:
      - KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181
      - ALLOW_PLAINTEXT_LISTENER=yes
      - KAFKA_BROKER_ID=1
      - KAFKA_CFG_LISTENERS=PLAINTEXT://:9092
      - KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"

volumes:
  BW_DB:
    driver: local
```

---

## 🔄 Communication Flow

### **Sensor-Service**
- Fetches bins from `bin-service` every **2 hours**.
- Every **30 seconds**, randomly selects a bin and sends a `FillMessage` to Kafka (`bin-fill` topic).

### **Bin-Service**
- Listens to the `bin-fill` topic.
- Updates the bin’s fill level in **MongoDB**.
- If the fill level exceeds the threshold, creates a `FillAlert` and publishes it to another Kafka topic.

### **Other Services** *(future)*
- Can subscribe to the **alerts** topic to handle:
  - Notifications  
  - Routing  
  - Reporting  

---

## 🚀 Key Endpoints
Although Kafka handles fill updates asynchronously,  
`bin-service` still exposes **REST endpoints** for managing bins  
(as described in **Chapter 3**).

---
