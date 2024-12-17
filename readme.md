Here is the final version of the **README.md** incorporating the conceptual note and details about the demo app:

---

# **Cloud-Libra API Project**

Cloud-Libra is a conceptual library management API built with **Spring Boot**. It showcases design, architecture, and development skills while being containerized for deployment across various platforms, including **Docker**, **Kubernetes**, **AWS ECS Fargate**, and **GCP Cloud Run**.

---

## **Live Demo**

You can check the **demo application** hosted on my home server (running via **Docker** and exposed via **Cloudflared**):  
[https://cloud-libra.ariefwara.my.id](https://cloud-libra.ariefwara.my.id)

---

## **Project Scope**

### **Table Definitions**

| Table Name    | Purpose                                | Key Columns                        |
|---------------|----------------------------------------|------------------------------------|
| **book**      | Represents catalog entries for books.  | `book_id`, `title`, `isbn`, `summary` |
| **book_copy** | Represents physical copies of books.   | `copy_id`, `book_id`, `qr_code`    |
| **patron**    | Represents library members/patrons.    | `patron_id`, `name`, `email`       |
| **borrow**    | Tracks book borrowing transactions.    | `borrow_id`, `copy_id`, `patron_id`, `borrow_date`, `due_date` |

## **Run the Application**

This project is **conceptual** and has not been tested in production environments. For quick execution and testing locally, use the prebuilt Docker image:

```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://<db_host>:<db_port>/<db_name> \
  -e SPRING_DATASOURCE_USERNAME=<your_db_username> \
  -e SPRING_DATASOURCE_PASSWORD=<your_db_password> \
  ariefwara/cloud-libra
```

### **Environment Variables**

| Variable                      | Description                    | Example                        |
|-------------------------------|--------------------------------|--------------------------------|
| `SPRING_DATASOURCE_URL`       | JDBC URL for the PostgreSQL DB | `jdbc:postgresql://localhost:5432/library` |
| `SPRING_DATASOURCE_USERNAME`  | Database username              | `postgres`                     |
| `SPRING_DATASOURCE_PASSWORD`  | Database password              | `password123`                  |

## **Database Setup**

1. Ensure **PostgreSQL** is running locally or remotely.
2. Execute the provided `ddl.sql` file to set up the required database schema.

## **CI/CD and Deployment**

The project is integrated with **GitHub Actions** for CI/CD automation, including build, test, and deployment pipelines.