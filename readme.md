# **Cloud-Libra API Project**

Cloud-Libra is a conceptual library management API built with **Spring Boot**.

## **Live Demo**

Explore the API and interact using **Swagger UI**:  
👉 [https://cloud-libra.ariefwara.my.id/swagger-ui/index.html#/](https://cloud-libra.ariefwara.my.id/swagger-ui/index.html#/)

---

## **Tech Stack**

- **Java 17**: Modern, secure, and performant.  
- **Spring Boot 3.4.0**: Rapid development of REST APIs.  
- **PostgreSQL**: Reliable and ACID-compliant database.  
- **SpringDoc OpenAPI**: Auto-generate API documentation with Swagger UI.  
- **Maven**: Project dependency and build management.  
- **JUnit 5 & Mockito**: Testing framework for unit tests.  
- **JaCoCo**: Code coverage reporting (80% minimum threshold).  
- **Docker**: Containerization for ease of deployment.

---

## **Features**

1. **Register Borrowers**: Add new borrowers to the system.  
2. **Manage Books**: Add books, retrieve all books, and manage borrowing or returning.  
3. **Validation and Error Handling**: Ensures data integrity and provides clear error responses.  
4. **API Documentation**:  
   Swagger UI is auto-generated for testing and interacting with the API.  

---

## **API Overview**

| **Method** | **Endpoint**                         | **Description**                          |
|------------|--------------------------------------|------------------------------------------|
| `POST`     | `/api/borrowers`                    | Register a new borrower.                 |
| `POST`     | `/api/books`                        | Register a new book.                     |
| `GET`      | `/api/books`                        | Get a paginated list of books.           |
| `GET`      | `/api/books/all`                    | Get a list of all books (no pagination). |
| `PUT`      | `/api/books/{bookId}/borrow/{borrowerId}` | Borrow a book.                           |
| `PUT`      | `/api/books/{bookId}/return`        | Return a borrowed book.                  |

---

## **Detailed Explanation**

### **1. `POST /api/borrowers`**
- **Purpose**: Registers a new borrower in the library system.  
- **Input**: A **JSON request body** containing borrower details: `name` and `email`.  
- **Output**: Returns the registered borrower’s information, including a unique `borrowerId`.  
- **Validation**: Ensures that `name` and `email` are valid and not empty.  
- **Use Case**: Adds a new user (borrower) to the system who can borrow books.  

---

### **2. `POST /api/books`**
- **Purpose**: Registers a new book in the library system.  
- **Input**: A **JSON request body** containing book details: `isbn`, `title`, and `author`.  
- **Output**: Returns the registered book’s information, including a unique `bookId`.  
- **Validation**: Ensures no conflicts occur with an existing book's ISBN, title, or author.  
- **Use Case**: Adds a new book to the library catalog.  

---

### **3. `GET /api/books`**
- **Purpose**: Retrieves a **paginated list of books** in the library.  
- **Input**: Query Parameters:  
   - `page` (default = 0): Page number (zero-based).  
   - `size` (default = 10): Number of books per page.  
- **Output**: Returns a paginated list of books with their details:  
  - `bookId`, `isbn`, `title`, `author`, and `borrowerId` (if applicable).  
- **Use Case**: Provides users or administrators with an overview of books in the library, using pagination for scalability.  

---

### **4. `GET /api/books/all`**
- **Purpose**: Retrieves a **complete list of all books** without pagination.  
- **Input**: No input required.  
- **Output**: Returns a list of books with their details:  
  - `bookId`, `isbn`, `title`, `author`, and `borrowerId` (if applicable).  
- **Use Case**: Useful for scenarios where all books need to be retrieved at once (e.g., exporting data, admin dashboards).  

---

### **5. `PUT /api/books/{bookId}/borrow/{borrowerId}`**
- **Purpose**: Allows a borrower to **borrow a specific book**.  
- **Input**:  
   - `bookId`: Path parameter identifying the book to borrow.  
   - `borrowerId`: Path parameter identifying the borrower.  
- **Output**: Returns the updated book details with the `borrowerId` set to the borrower.  
- **Validation**: Ensures the book is not already borrowed.  
- **Use Case**: Marks a book as borrowed by a specific borrower.

---

### **6. `PUT /api/books/{bookId}/return`**
- **Purpose**: Allows a borrower to **return a borrowed book**.  
- **Input**:  
   - `bookId`: Path parameter identifying the book being returned.  
- **Output**: Returns the updated book details with `borrowerId` set to `null`, indicating the book is now available.  
- **Validation**: Ensures the book is currently marked as borrowed.  
- **Use Case**: Marks a book as returned and available for other borrowers.  

---

## **Setup**
Here’s the refined **Run Locally** section based on your requirement:  

- **Maven**: Use this for **development**, code modification, or testing the application.  
- **Docker Compose**: Use this to quickly run the **finished product** in a simple and consistent environment.  

---

### **Run Locally**

1. **Clone the Repository**
   ```bash
   git clone https://github.com/ariefwara/cloud-libra.git
   cd cloud-libra
   ```

2. **Database Setup**  
   - Install PostgreSQL on your host machine.  
   - Create a database and apply the schema:
     ```bash
     psql -U <username> -d <database_name> -f src/main/resources/schema.sql
     ```

---

#### **Option 1: For Development (Maven)**

Use this option if you need to **develop**, **test**, or make code modifications:

1. Ensure PostgreSQL is running locally.  

2. Run the application using Maven:
   ```bash
   mvn spring-boot:run
   ```

3. Access the application:  
   - **API Base URL**: [http://localhost:8080](http://localhost:8080)  
   - **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

#### **Option 2: For Simple Environment (Docker Compose)**  

Use this option to quickly run the **finished product** in a controlled environment:

1. Navigate to the `deploy/local` folder:
   ```bash
   cd deploy/local
   ```

2. Run the application with Docker Compose:
   ```bash
   docker-compose up
   ```

3. Access the application:  
   - **API Base URL**: [http://localhost:8402](http://localhost:8402)  
   - **Swagger UI**: [http://localhost:8402/swagger-ui.html](http://localhost:8402/swagger-ui.html)

---

## **Test and Code Coverage**

- **Run Unit Tests**:  
   ```bash
   mvn test
   ```

- **Generate Code Coverage Report**:  
   ```bash
   mvn verify
   ```  
   The JaCoCo report will be available under `target/site/jacoco`.

---

## **Requirements Checklist**

| **Requirement**                                                                                  | **Check** |
|--------------------------------------------------------------------------------------------------|-----------|
| **Core Functionalities**                                                                         |           |
| 1. Register a new borrower to the library.                                                       | ✔         |
| 2. Register a new book to the library.                                                           | ✔         |
| 3. Get a list of all books in the library.                                                       | ✔         |
| 4. Borrow a book with a particular `bookId`.                                                     | ✔         |
| 5. Return a borrowed book.                                                                       | ✔         |
|                                                                                                  |           |
| **Data Models**                                                                                  |           |
| - Borrower: Unique ID, name, and email address.                                                  | ✔         |
| - Book: Unique ID, ISBN number, title, and author.                                               | ✔         |
| - Two books with the same title and author but different ISBNs are considered different books.   | ✔         |
| - Books with the same ISBN must have identical `title` and `author`.                             | ✔         |
| - Allow multiple copies of books with the same ISBN.                                             | ✔         |
|                                                                                                  |           |
| **Technical Requirements**                                                                       |           |
| 1. Use Java 17 and Spring Boot framework.                                                        | ✔         |
| 2. Configurable to run in multiple environments (via Helm Variables).                            | ✔         |
| 3. Use a package manager (Maven).                                                                | ✔         |
| 4. Implement proper data validation (Hibernate Validator).                                       | ✔         |
| 5. Implement error handling (Global Exception Handler).                                          | ✔         |
| 6. Use PostgreSQL for storing borrower and book data.                                            | ✔         |
| 7. REST API endpoints implemented for all actions.                                               | ✔         |
| 8. Books with the same `ISBN` are registered as separate books with unique IDs.                  | ✔         |
| 9. Ensure only one borrower can borrow a book at a time.                                         | ✔         |
| 10. Provide clear documentation (Swagger UI + README).                                           | ✔         |
| 11. Document assumptions for any unspecified requirements.                                       | ✔         |
|                                                                                                  |           |
| **Nice to Have**                                                                                 |           |
| 1. Include unit tests and code coverage (JUnit 5 + JaCoCo with 80% threshold).                   | ✔         |
| 2. Demonstrate clean code (Adherence to best practices).                                         | ✔         |
| 3. Use containerization (Docker) and CI/CD tools (Github Actions).                               | ✔         |
| 4. Demonstrate conformance to 12 Factor Application principles (config, portability, stateless). | ✔         |

---

## **Assumptions**

1. Books with the **same ISBN** must have the same `title` and `author`.  
2. Multiple copies of the same book (same ISBN) can exist but with different unique IDs.  
3. A book can only be borrowed by **one borrower** at a time.