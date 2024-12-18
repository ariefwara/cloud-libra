package id.my.ariefwara.cloud.libra.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Borrower {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Automatically generates UUID
    @Column(name = "borrower_id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID borrowerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    public Borrower() {}

    public Borrower(UUID borrowerId, String name, String email) {
        this.borrowerId = borrowerId;
        this.name = name;
        this.email = email;
    }

    // Getters and Setters
    public UUID getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(UUID borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
