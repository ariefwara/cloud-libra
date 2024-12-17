# **Cloud-Libra API Project**

Cloud-Libra is a library management API project built with **Spring Boot**. The application is containerized using **Docker** and is designed to be deployable on multiple platforms, such as **Docker**, **Kubernetes**, **AWS ECS Fargate**, and **GCP Cloud Run**. This project demonstrates skills in architecting, designing, and developing scalable systems.

---

## **Project Scope**

### **Table Definitions**

| Table Name    | Purpose                                | Key Columns                        |
|---------------|----------------------------------------|------------------------------------|
| **book**      | Represents catalog entries for books.  | `book_id`, `title`, `isbn`, `summary` |
| **book_copy** | Represents physical copies of books.   | `copy_id`, `book_id`, `qr_code`    |
| **patron**    | Represents library members/patrons.    | `patron_id`, `name`, `email`       |
| **borrow**    | Tracks book borrowing transactions.    | `borrow_id`, `copy_id`, `patron_id`, `borrow_date`, `due_date` |

---

## **Live Demo**

Before setting up, you can check the running instance of the application:  
[https://cloud-libra.ariefwara.my.id](https://cloud-libra.ariefwara.my.id)

---

## **Run the Application**

To quickly run the application, use the prebuilt Docker image hosted on Docker Hub:

```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://<db_host>:<db_port>/<db_name> \
  -e SPRING_DATASOURCE_USERNAME=<your_db_username> \
  -e SPRING_DATASOURCE_PASSWORD=<your_db_password> \
  ariefwara/cloud-libra
```

---

### **Environment Variables**

| Variable                      | Description                    | Example                        |
|-------------------------------|--------------------------------|--------------------------------|
| `SPRING_DATASOURCE_URL`       | JDBC URL for the PostgreSQL DB | `jdbc:postgresql://localhost:5432/library` |
| `SPRING_DATASOURCE_USERNAME`  | Database username              | `postgres`                     |
| `SPRING_DATASOURCE_PASSWORD`  | Database password              | `password123`                  |

---

## **Database Setup**

1. Ensure **PostgreSQL** is running.
2. Execute the `ddl.sql` file to set up the database schema.

---

## **CI/CD and Deployment**

The project uses **GitHub Actions** for CI/CD automation, including building, testing, and deploying the application.