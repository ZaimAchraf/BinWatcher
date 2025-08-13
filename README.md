# 🐳 Chapter 10 – Dockerization

This chapter focuses on making the application fully deployable using **Docker**. Each service is containerized and configured to run independently while communicating over defined Docker networks.

---

## 🔹 Dockerfiles for Services

Each service has its own **Dockerfile**, typically structured in two stages:

1. **Build Stage** – Uses a Maven image to build the JAR file without running tests.
   ```dockerfile
   FROM maven:3.8.5-openjdk-17 AS builder
   WORKDIR /build
   COPY api-module api-module
   COPY business/notification-service business/notification-service
   RUN mvn clean install -f api-module/pom.xml
   RUN mvn clean package -f business/notification-service/pom.xml -DskipTests
   ```
1. **Runtime Stage** – Uses an OpenJDK image to run the compiled JAR..
   ```dockerfile
   FROM openjdk:17-jdk-alpine
   WORKDIR /app
   COPY --from=builder /build/business/notification-service/target/*.jar app.jar
   ENTRYPOINT ["java", "-jar", "app.jar"]
   ```
For **api-module**, Maven is configured to skip execution and only create the JAR:
```xml
<configuration>
    <skip>true</skip>
</configuration>
```

## 🔹 Docker Compose

The `docker-compose.yml` orchestrates all services, including **infrastructure** and **application services**.

### Infrastructure Services
- **MongoDB** – Centralized storage for all services, with initial root credentials and volume mapping.
- **Zookeeper & Kafka** – Messaging and coordination services.
- **Elasticsearch, Logstash, Kibana (ELK)** – For centralized logging.
- **Consul** – Service discovery and configuration management.
- **MailDev** – SMTP server container for testing email notifications without spamming real mailboxes.

### Application Services
Each microservice runs in its own container, connected via `app_network`:

| Service                      | Port  | Notes                                               |
|-------------------------------|-------|---------------------------------------------------|
| config-service               | 9090  | Configuration server for all services             |
| gateway-service              | 8080  | JWT_SECRET configured for authentication          |
| security-service             | 8081  | Handles user accounts, connected to MongoDB       |
| admin-service                | 8082  | Admin functionalities                              |
| bin-service                  | 8083  | Connects to BW_BINS_DB                             |
| sensor-service               | 8084  | Connects to configuration service                 |
| driver-service               | 8085  | Connects to driver MongoDB; creates new accounts  |
| driver-assignment-service    | 8086  | Handles assignments; depends on driver-service    |
| notification-service         | 8087  | Sends notifications; depends on bin-service & MailDev |

#### Example Environment Variables
- `JWT_SECRET` – Secret used for creating and verifying JWT tokens.
- `SECURITY_MONGO_URI` – MongoDB connection URI for the security-service.
- `BIN_MONGO_URI` – MongoDB connection URI for the bin-service.
- `DRIVER_MONGO_URI` – MongoDB connection URI for the driver-service.
- `ASSIGNMENT_MONGO_URI` – MongoDB connection URI for the driver-assignment-service.
- `NOTIF_MONGO_URI` – MongoDB connection URI for the notification-service.
- `SPRING_ACTIVE_PROFILE` – Defines the Spring profile (e.g., `dev` or `prod`).

```yaml
environment:
  - DRIVER_MONGO_URI=mongodb://admin:admin@mongodb:27017/BW_DRIVER_DB?authSource=admin
  - SPRING_ACTIVE_PROFILE=dev
```

### Networks
- **app_network** – Main network for communication between application and infrastructure services.
- **elk** – Network for ELK stack.

### Volumes
- **BW_DB** – Persistent storage for MongoDB.

## 🔹 Benefits of Dockerization
- **Isolation** – Each service runs in its own container with all dependencies.
- **Reproducibility** – Same environment across machines and deployments.
- **Simplified Infrastructure** – All dependencies (MongoDB, Kafka, ELK, MailDev, Consul) run as containers.
- **Development-friendly** – MailDev prevents sending emails to real accounts.
- **Scalable & Portable** – Containers can easily be moved to different environments or cloud providers.

## 🔹 Notes
- Consul has been migrated to a container instead of running locally.
- MailDev is used for email testing to avoid sending real emails during development.
- Ports and dependencies are managed through Docker Compose to ensure proper startup order (`depends_on`).

> **Note:** If you want to run the application from this branch, simply run the command:
> 
> ```bash
> docker-compose up --build
> ```
