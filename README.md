# 🛒 Shopping Cart System (Scala)

A simple, modular **Shopping Cart system** built using **Scala**, demonstrating clean architecture, functional design, and testable service layers.

---

## 🚀 Overview

This project models a basic e-commerce cart system with support for:

- Product management
- Cart creation and updates
- Adding/removing items
- Price calculation
- Unit testing for core services

The focus is on **separation of concerns**, **clean code**, and **functional programming principles**.

---

## 🧱 Architecture

The project follows a layered design:

```
domain   → core business models
service  → business logic
main     → entry point
test     → unit tests
```

### 📦 Modules

- **Domain Layer**
  - `Product`
  - `CartItem`
  - `Cart`

- **Service Layer**
  - `ProductService` → manages product data
  - `CartService` → handles cart operations

- **Main**
  - Application entry point

- **Tests**
  - Unit tests for core services

---

## 🛠 Tech Stack

- Scala (2.13)
- SBT (build tool)
- Functional programming patterns
- Unit Testing (ScalaTest / JUnit)

---

## ▶️ Getting Started

### Prerequisites
- Java 8+
- SBT installed

### Run the application
```bash
sbt run
```

### Run tests
```bash
sbt test
```

---

## 🧪 Testing

Unit tests cover:
- Cart operations
- Product service logic

---

## 💡 Key Design Decisions

- **Separation of concerns**
  - Domain models are independent of business logic

- **Service-based design**
  - Business logic is encapsulated in services

- **Immutability**
  - Promotes safer and predictable behavior

- **Testability**
  - Core logic is validated via unit tests

---

## 🔧 Possible Improvements

- Add REST API layer (Akka HTTP)
- Persist data (PostgreSQL / MongoDB)
- Introduce concurrency (Actors / Futures)
- Add pricing rules, discounts, promotions
- Dockerize the application

---

## 📌 Purpose

Built for **learning and demonstrating backend design skills**, including:
- Clean architecture
- Functional programming in Scala
- Testable service design

---
