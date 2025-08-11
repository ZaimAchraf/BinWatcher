# 🔐 Chapter 2: Security Implementation (Authentication & Authorization)

## 🌟 **Chapter Summary**
This chapter presents the implementation of the security service, which handles:
- **User registration (signup)**
- **Authentication** with Spring Security & JWT
- **Role validation** at the API Gateway level
- **Secured route filtering**

The goal is to provide a **stateless**, secure, and easily extensible system for all microservices.

---

## ⚙️ **Implemented Components**

### 1. **Database & Deployment**
- Database running inside a **Docker container**
- `User` table with roles (`Authority`)
- Spring Data JPA repository for user management

#### **Running the Database**
A `docker-compose.yml` file is provided to simplify database setup.  
To start the database, run:
```bash
docker-compose up -d
```
---

### 2. **Spring Security & JWT**
#### **Security Configuration (`AuthConfig`)**
- Disabled **CSRF** to simplify API calls
- Public access allowed for `/auth/**`  
- **STATELESS** session mode
- **AuthenticationProvider** based on `DaoAuthenticationProvider`
- Password management with **BCryptPasswordEncoder**

#### **JWT Utility (`JWTUtil`)**
- Generates and validates signed tokens using a secret key (`HS256`)
- Stores roles and username in the payload
- Checks for expiration
- Extracts claims: `username`, `roles`, `expiration`

---

### 3. **Authentication & Registration**
#### **Service (`AuthService`)**
- **`register`**: creates a new user with an encrypted password and assigned roles
- **`login`**: authenticates the user, generates a JWT with their roles, and returns it in the response
- Handles exceptions if the email is already in use

---

### 4. **Gateway-Level Security**
#### **Authentication Filter (`AuthFilter`)**
- Checks for the presence of the `Authorization` header
- Extracts and validates the JWT
- Rejects requests if the token is invalid or if the user does not have the required permissions
- Relies on `RouteValidator` for role validation

#### **Route Validation (`RouteValidator`)**
- List of public URLs (`/auth/register`, `/auth/login`)
- Mapping **URL → Allowed Roles**:
    ```java
    {
        "/admin" : ["ADMIN"],
        "/driver" : ["ADMIN", "DRIVER"],
        "/test/admin" : ["ADMIN"]
    }
    ```
- Verifies that the roles from the token match the required roles

---

## 🧪 **Global Flow**
1. **Registration**: The user calls `/auth/register` → password is encrypted → user is saved in the database  
2. **Login**: The user calls `/auth/login` → JWT is generated containing the user’s roles  
3. **Accessing a secured service**:  
   - Gateway intercepts the request via `AuthFilter`
   - Validates the token and roles
   - Allows or denies the request based on the rules
