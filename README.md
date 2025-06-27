# 🛒 EasyShop API

## 📌 Project Overview

**EasyShop** is a simple e-commerce RESTful API built with Java and Spring Boot.  
It supports:
- User and admin authentication
- Product CRUD operations
- Order placement and tracking

This API is the backend for a full-stack shopping app.

---

## 🖼️ Application Screenshots

[Login Screen]

![login](https://github.com/user-attachments/assets/ce27cbb7-ffb3-4524-8b20-b3d0ff5bd855)

[Product List]

![product-list](https://github.com/user-attachments/assets/ac749407-667d-4d70-863a-9a0fae2c5129)


---

## 🧱 Architecture Overview

```mermaid
flowchart LR
    Client -->|HTTP| Controller
    Controller --> Service
    Service --> Repository
    Repository --> Database[(MySQL)]


