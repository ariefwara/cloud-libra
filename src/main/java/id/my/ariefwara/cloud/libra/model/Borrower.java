package id.my.ariefwara.cloud.libra.model;

import jakarta.persistence.*;

@Entity
@Table(name = "borrower")
public record Borrower(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "borrower_id")
    Long borrowerId,

    @Column(name = "name", nullable = false)
    String name,

    @Column(name = "email", nullable = false)
    String email
) {}
