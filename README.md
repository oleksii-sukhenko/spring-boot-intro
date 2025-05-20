# BookStore

Welcome to **BookStore**, a Java-based RESTful web application designed to manage a simple online bookstore. This is an introductory project that demonstrates the core principles of Spring Boot and backend development using modern technologies.

---

## 🚀 Introduction

BookStore was created to simulate the essential operations of a real-world online bookstore. Inspired by the need to understand and implement a full-stack backend solution, this project highlights key skills and technologies required in enterprise-grade applications.

It provides a solid foundation for beginners looking to grasp concepts like authentication, authorization, data persistence, REST APIs, and containerization with Docker.

---

## 📊 Technologies Used

* **Java 17**
* **Spring Boot 3.2**
* **Spring Security**
* **Spring Data JPA**
* **Hibernate Validator**
* **MapStruct 1.5** for mapping between DTOs and entities
* **Liquibase 4.27** for database version control
* **MySQL 8** (deployed via Amazon RDS)
* **Docker & Docker Compose**
* **Swagger / OpenAPI** for API documentation
* **Checkstyle** for code quality
* **GitHub Actions** for CI/CD pipeline

---

## 🚛 Features and Functionalities

### Authentication & Authorization

* Register a new user with a default role `USER`
* Login and receive JWT token
* Role-based access to endpoints (admin/user)

### Book Management

* **Admins** can:

    * Add new books
    * Update existing books
    * Delete books
* **Users** can:

    * View the list of books
    * Search books by title, author, or category

### Category Management

* Admins can create and manage book categories

### Shopping Cart

* Authenticated users can:

    * Add books to their cart
    * View and modify cart contents

### Orders

* Place an order from the shopping cart
* View order history

---

## 📁 Project Structure

```bash
bookstore/
├── src/main/java/com/bookstore
│   ├── config          # Security and Swagger config
│   ├── controller      # REST controllers
│   ├── dto             # Data Transfer Objects
│   ├── exception       # Custom exceptions and handlers
│   ├── model           # JPA entity classes
│   ├── repository      # Spring Data repositories
│   ├── security        # Security-related logic
│   ├── service         # Business logic layer
│   └── util            # Utility classes
├── Dockerfile
├── docker-compose.yml
├── .env.sample
├── checkstyle.xml
└── pom.xml
```

---

## 📅 How to Run This Project

### Prerequisites

* Java 17
* Maven
* Docker
* AWS RDS MySQL instance or local MySQL

### 1. Clone the repository

```bash
git clone https://github.com/oleksii-sukhenko/spring-boot-intro.git
cd spring-boot-intro
```

### 2. Configure Environment

Create an `.env` file or pass environment variables:

```env
SPRING_DATASOURCE_URL=jdbc:mysql://your-db-endpoint:3306/bookstore
SPRING_DATASOURCE_USERNAME=your-username
SPRING_DATASOURCE_PASSWORD=your-password
```

### 3. Build the project

```bash
./mvnw clean install
```

### 4. Run with Docker Compose

```bash
docker-compose up --build
```

### 5. Access the app

* API base URL: `http://localhost:8080`
* Swagger UI: `http://localhost:8080/api/swagger-ui/index.html`

---

## 🌀 API Documentation

Interactive documentation available via Swagger UI:

* Local:
```
http://localhost:8080/api/swagger-ui/index.html
```
* Production:
```
http://ec2-3-83-161-155.compute-1.amazonaws.com/api/swagger-ui/index.html#/
```

---

## 🚨 Challenges Faced

* **Docker DNS resolution errors**: Solved by configuring custom DNS and restarting Docker service properly on AWS EC2.
* **Access issues with AWS ECR**: Fixed authentication errors using correct login with `aws ecr get-login-password`.
* **JWT token authorization**: Ensured correct filter order and security configuration to protect secured endpoints.
* **Testing with real DB (RDS)**: Used Testcontainers and separated test configuration for isolated integration testing.

---

## 🔹 Postman Collection

A Postman collection is available to test endpoints. You can import the collection and use predefined requests for:

* Registration
* Login
* CRUD operations for books
* Order flow

[Download Postman Collection](https://www.postman.com/telecoms-architect-81065355/sukhenko-bookstore/collection/05s5t67/localhost-8080?action=share&creator=38039570)

---

## 📹 Loom Demo

Watch the demo video to see how everything works in action: [BookStore Loom Video Demo](https://www.loom.com/share/e1c59c00de524a5f91a2a789424b1091?sid=5350be10-3f27-49d1-ae83-310c97e9c907)

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

If you find this project helpful or interesting, feel free to star the repo and connect with me on [LinkedIn](https://www.linkedin.com/in/oleksii-sukhenko/).
