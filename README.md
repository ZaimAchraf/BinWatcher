# 🛠️ Chapter 3: Bin-Service & Admin-Service Microservices

## 🌟 **Chapter Summary**
This chapter presents the architecture and implementation of two key microservices:
- **bin-service**: manages bins with full CRUD operations and business validation
- **admin-service**: central microservice for the admin dashboard, which consumes the bin-service and will be extended to manage other entities like drivers

---

## ⚙️ **Architecture & Features**

### 1. **Bin-Service**
- Exposes a full REST API to manage bins: create, update, delete, and update fill level  
- Validates incoming data (location, coordinates, alert thresholds)  
- Persists data in a MongoDB database hosted in a **Docker container**  
- Communicates with other services using a **Feign Client** to simplify HTTP calls  

### 2. **Admin-Service**
- Serves as the backend interface for the admin dashboard  
- Uses the **Feign Client** `BinsClient` to interact with the bin-service  
- Manages business logic on the admin side  
- Extensible to include other features, notably driver management

---

## 📡 **OpenFeign** (Summary)
OpenFeign is a Java library that facilitates inter-microservice communication over HTTP.  
It allows defining REST clients as annotated Java interfaces, removing the need to manually handle HTTP requests.  
In this project, it is used in `admin-service` to call the bin-service via the `BinsClient` interface.

---

## 🗄️ **Database**
The MongoDB database runs inside a **Docker container**, as explained in Chapter 2.

---

## 🔄 **Communication Flow**

1. The dashboard (through admin-service) sends requests to `BinsService`  
2. `BinsService` uses `BinsClient` (Feign) to call the REST APIs of bin-service  
3. bin-service processes the request, applies business logic, and interacts with MongoDB  
4. The response is sent back up to the dashboard

---

## 🚀 **Main Endpoints**

### Bin-Service (exposed via REST)
- `GET /api/bins`: retrieve the list of bins  
- `POST /api/bins`: create a new bin  
- `PUT /api/bins/{id}`: update an existing bin  
- `PATCH /api/bins/{id}/fill-level`: update the fill level  
- `DELETE /api/bins/{id}`: delete a bin

### Admin-Service
- Uses the methods exposed in `BinsClient` to communicate with the bin-service  

---
