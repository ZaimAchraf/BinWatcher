# 🧪 Chapter 09 – Testing

This chapter focuses on testing the different modules of the application to ensure correctness, stability, and proper behavior of services and controllers.

---

## 🔹 Unit Tests

Unit tests verify individual classes or services in isolation. Dependencies are mocked using **Mockito**, so the tests do not rely on external systems like databases, APIs, or message brokers.

**Examples of unit tests:**

- **BinsServiceTest** – Tests methods like `getAll()`, `create()`, `update()`, `updateFillLevel()`, and `delete()`. Mocks repository, producers, and helper classes.
- **DriverServiceTest** – Tests methods like `getAll()`, `getById()`, `create()`, `updateCoordinates()`, and `delete()`. Mocks repository and user client.
- **NotificationServiceTest** – Tests sending notifications. Mocks `MailSenderService` and `BinClient`.
- **AssignmentServiceTest** – Tests bin assignments and disabling assignments. Mocks repositories, clients, and producers.

---

## 🔹 Integration-style Tests (Controller)

These tests verify the behavior of **controllers** using Spring’s **MockMvc**. They test the endpoints by performing HTTP requests and verifying responses.

**Examples:**

- **BinControllerTest** – Tests endpoints like:
  - `GET /bins`
  - `POST /bins`
  - `PUT /bins/{id}`
  - `PATCH /bins/{id}/fill-level`
  - `DELETE /bins/{id}`  
  Mocks the underlying service (`BinsService`) and verifies responses.

---

## 🔹 Testing Approaches Used

- **Mocking Dependencies** – All external calls (database, REST clients, message producers) are mocked to focus on the unit under test.
- **Assertions** – Verify returned values, status codes, and side effects (like sending notifications or saving entities).
- **Exception Handling** – Tests include failure cases to verify proper exceptions are thrown.
- **Argument Captors** – Used in some cases (e.g., `AssignmentServiceTest`) to capture method arguments and validate values.

---

## ✅ Benefits

- Ensures business logic behaves as expected.
- Validates edge cases and error handling.
- Prevents regressions during future code changes.
- Supports a maintainable and reliable codebase.
