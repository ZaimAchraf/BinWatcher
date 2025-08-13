# BinWatcher

BinWatcher is a learning application based on a microservices architecture to manage bins, drivers, and their assignments. The application simulates the management of bin fill levels and the assignment of drivers for collection.

![Bin_Watcher_Diagramme drawio](https://github.com/user-attachments/assets/49cb41f0-aea2-489c-a5f1-98b97230aba1)

---

## 🚀 Main Features

- **bin-service**: Main service for managing bins. Allows creating, retrieving, and updating bins.
- **driver-service**: Service for managing drivers. Allows creating, retrieving, and managing driver information.
- **admin-service**: Service used by the admin to query `bin-service` and `driver-service` to manage bins and drivers.
- **sensor-service**: Simulates bin sensors by generating random fill levels and sending messages to a Kafka topic.
- **driver-assignment-service**: Analyzes fill-level alerts, finds the closest available drivers, and assigns bins to drivers.
- **notification-service**: Sends email notifications to drivers when a bin is assigned or unassigned.

---

## 🧑‍💼 Business Workflow

1. **Generating fill levels**:
   - **sensor-service** retrieves the list of bins from **bin-service**.
   - Then, it generates random fill levels for each bin and publishes this information to the Kafka topic **bin-fill**.

2. **Checking and updating fill levels**:
   - **bin-service** listens to the **bin-fill** topic and receives messages containing bin fill levels.
   - When a message is received, **bin-service** updates the corresponding bin’s fill level.
   - If the fill level exceeds a predefined threshold, **bin-service** publishes a message to the **fill-alert** topic to alert other services.

3. **Driver assignment**:
   - **driver-assignment-service** listens to **fill-alert** and receives alerts for bins that exceeded the threshold.
   - For each alert, it analyzes the bin location and compares it with the list of available drivers.
   - The service assigns the closest driver to collect the bin.
   - A message is then published to the **assignment-notif** topic to notify other services of the assignment.

4. **Sending notifications**:
   - **notification-service** listens to **assignment-notif** and receives messages about assigned or unassigned bins.
   - When a message is received, it sends an email to the driver informing them of the assignment or unassignment.

5. **Unassigning bins**:
   - If **bin-service** receives a message showing a bin’s fill level below the threshold that was previously assigned, it calls **driver-assignment-service** via OpenFeign to unassign the bin.
   - **driver-assignment-service** unassigns the driver and sends a message to **notification-service**, which notifies the driver via email.

---

## 🧱 Technical Architecture

The application uses the following technologies:

- **Spring Boot**: Main framework.  
- **Kafka**: Asynchronous communication between services using topics.  
- **Consul**: Each service automatically registers to enable service discovery and inter-service communication.  
- **Config Server**: Services retrieve their configuration from the `config-server`, which fetches it from a central configuration repository.  
- **Docker & Docker Compose**: Containerization of services for simplified deployment.  
- **MongoDB**: Database used by services that need to store data.  
- **Maildev**: Development SMTP server for sending emails.  
- **ELK (Elasticsearch, Logstash, Kibana)**: Centralized logging.  
- **Gateway Service**: Entry point for all external calls.  
- **Security Service**: Manages user authentication and registration.  
- **Config Service**: Centralized configuration for microservices.

> During runtime, all services register with **Consul** for automatic service discovery and retrieve configurations from the **config-server**, which loads them from a centralized repository.

---

## 📚 Branches and Chapters Structure

Each branch corresponds to a chapter, introducing a specific concept or feature:

- **Chapter 01** – Microservices Technical Architecture  
- **Chapter 02** – Security Implementation (Authentication & Authorization)  
- **Chapter 03** – Bin-Service & Admin-Service Microservices  
- **Chapter 04** – Bin Filling Management with Asynchronous Communication via Kafka  
- **Chapter 05** – Driver Assignment Service  
- **Chapter 06** – Notification Service for Driver Alerts  
- **Chapter 07** – Code Improvements & Best Practices  
- **Chapter 08** – Logging with ELK (Elasticsearch, Logstash, Kibana)  
- **Chapter 09** – Testing  
- **Chapter 10** – Dockerization (corresponds to the Docker-Deployment branch)  

> Each chapter contains the corresponding code, documentation, and tests related to the presented functionality or concept.

---

## 📧 Contact

Created by **Zaim Achraf** for learning purposes.  
For questions or suggestions, contact me on [LinkedIn](https://www.linkedin.com/in/achraf-zaim-443936233/).

---
