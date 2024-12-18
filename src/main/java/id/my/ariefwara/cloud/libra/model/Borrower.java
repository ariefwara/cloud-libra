package id.my.ariefwara.cloud.libra.model;

import jakarta.persistence.*;

@Entity
public class Borrower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long borrowerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    // Default constructor required by JPA
    public Borrower() {}

    public Borrower(Long borrowerId, String name, String email) {
        this.borrowerId = borrowerId;
        this.name = name;
        this.email = email;
    }

    // Getters and Setters
    public Long getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(Long borrowerId) {
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
