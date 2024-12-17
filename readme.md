# **Cloud-Libra API Project**

Cloud-Libra is a conceptual library management API built with **Spring Boot**.

## **Live Demo**

You can check the **demo application** hosted on my home server:  
[https://cloud-libra.ariefwara.my.id](https://cloud-libra.ariefwara.my.id)

| **Category**                     | **Assumption/Choice**                                     | **Rationale**                                                                                     |
|----------------------------------|----------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| **Clean Code Standard**          | Use **Uncle Bob's Clean Code** principles                | The requirement emphasizes "demonstrating clean code," and Uncle Bob's principles ensure maintainability, readability, and clarity. |
| **Multi-Environment Setup**      | Multi-environment means **multi-test stages**            | The requirement asks for "configurable to run in multiple environments," focusing on test stages (e.g., dev, test, staging, prod). |
| **Image Promotion**              | Use a **single Docker image** for promotion              | Aligns with containerization best practices and avoids rebuilding images, ensuring consistent deployment across environments. |
| **Database Choice**              | Use **PostgreSQL** instead of MySQL or document-based DB | The requirement specifies an RDBMS, and PostgreSQL is highly scalable, reliable, and adheres to ACID compliance for relational data. |
| **Dependency Management**        | Use **Maven** instead of Gradle                          | Maven aligns with Java and Spring Boot projects, meeting the requirement to "use a package manager" for dependency management. |
| **API Documentation**            | Use **Swagger** for API documentation                    | The requirement states "clear documentation for API usage," and Swagger provides standardized, interactive API documentation. |
| **Local Testing**                | Use **Docker** for local testing                         | The requirement mentions containerization; Docker ensures consistent local environments for development and testing. |
| **Environment Management**       | Use **Kubernetes** for test, staging, and production     | The requirement asks for "demonstrating containerization and CI/CD tools"; Kubernetes is the de facto platform for orchestration. |
| **Local Multi-Service Setup**    | Use **Docker Compose** to run on Docker                  | Simplifies multi-service execution (API, PostgreSQL) locally for testing, meeting "use a database" and modular setup requirements. |
| **K8s Deployment**               | Use **Helm Charts** to deploy on Kubernetes              | Meets the need for "declarative deployment" as Helm charts enable versioned, reusable configurations for Kubernetes environments. |
