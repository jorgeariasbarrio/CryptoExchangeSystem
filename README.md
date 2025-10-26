# 💱 Exchange Microservices Platform

This project implements a simulated cryto exchange with a microservice architecture design to let the users create an account, deposit some founds and exchange asset on the system.
It tries to simulate a pure exchange system:
1. A client creates an order. For example ( I want to sell X quantity of Y asset).
2. If it's posible to complete the order, the asset or the balance is reserved until the order processing.
3. Asynchronously, the order is match in the engine micro to try to complete the exchange with other client order ( for example, if client 1 is trying to sell 0.1 BTC, we will find other client who is looking to buy the concrete asset. 

---

## 🧩 Architecture Overview

The platform is composed of the following microservices:
 
- **order-service** → Handles client order creation, emits creation events, and stores order data.
- **user-service** → Manages user registration, authentication, and account management.
- **portfolio-service** → Core business logic for portfolio operations (balances, assets, movements, etc.).
- **engine-service** → Responsible for matching and executing exchanges between client orders.
Shared infrastructure components:
- **Kafka** (broker + Zookeeper)
- **MongoDB**

---

##  Requirements

Make sure you have the following installed before running the project:

- [Docker](https://www.docker.com/get-started) & [Docker Compose](https://docs.docker.com/compose/)
- JDK 17 or later
- Maven 3.8+ (for local builds)

---

## 🚀 Getting Started

### 🔹 1. Build all microservices

From the project root directory:

```bash
mvn clean package -DskipTests

docker compose up -l 

