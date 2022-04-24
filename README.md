# Jolie Case Study
This project consists in a case study using the programming language Jolie. Also a comparison with Spring Boot is made.

- [Jolie Case Study](#jolie-case-study)
- [Setup](#setup)
  - [Pre-requisites](#pre-requisites)
  - [Run the Jolie services](#run-the-jolie-services)
  - [Run the Spring Boot services](#run-the-spring-boot-services)
- [Description](#description)
- [Functional Requirements](#functional-requirements)
- [Non-functional Requirements](#non-functional-requirements)
- [Database](#database)

&nbsp;

# Setup
## Pre-requisites
- MySQL
- Postman
- Jolie
- Java
- Maven

## Run the Jolie services

To run the Jolie services do the following:

```shell
cd [PROJET_PATH]/jolie/services

jolie CartService.ol
```

```shell
cd [PROJET_PATH]/jolie/services

jolie EmailService.ol
```

```shell
cd [PROJET_PATH]/jolie/services

jolie PaymentService.ol
```

```shell
cd [PROJET_PATH]/jolie/services

jolie ProductService.ol
```

## Run the Spring Boot services

To run the Spring Boot services do the following:

```shell
cd [PROJET_PATH]/spring-boot/product-service

mvn spring-boot:run
```

&nbsp;

# Description
The proposed case study is an e-commerce application where users can buy products. The application is built based on microservices architecture style and in the application, users can browse items, add them to the cart, and purchase them.
There we be built two different solutions for the application implementation:
- One that will be built using **Jolie** as the orchestrator and as the microservices implementation 
- And another that will be using **Spring Boot** as the microservices implementation 

&nbsp;

# Functional Requirements
The below table shows the **functional requirements** for the project developed. The requirements have the goal to provide users with all the functionalities to support the entire process of online product buying from product browsing, buying, and shipping. 

| Identifier | Description |
| ---------- | ----------- |
| FR1 | Users can add items to their cart |
| FR2 | Users can remove items from their cart |
| FR3 | Users can browse products |
| FR4 | Users can change the currency used |
| FR5 | Users can buy products |
| FR6 | Users receive emails from the transactions done |
| FR7 | Users can provide shipping information |

&nbsp;

# Non-functional Requirements
The below table shows the **non-functional requirements** for the project which must be built having the microservices architecture where communication is handled using orchestrator and design patterns considered when needed.
Table 7 – Non-functional requirements of the application
| Identifier |	Description |
| ---------- | ----------- |
| NFR1 | Solution must use Jolie |
| NFR2 | Every microservice as their own database |
| NFR3 | The solution will be based on orchestration for service communication |
| NFR4 | Solution must be using the microservice system architecture and their principles need to be considered |

&nbsp;

# Database

Database password: Ia@bNf-9NAd!t(@z