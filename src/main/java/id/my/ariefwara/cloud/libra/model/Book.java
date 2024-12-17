package id.my.ariefwara.cloud.libra.model;

import jakarta.persistence.*;

@Entity
@Table(name = "book")
public record Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    Long bookId,

    @Column(name = "isbn")
    String isbn,

    @Column(name = "title", nullable = false)
    String title,

    @Column(name = "author")
    String author,

    @Column(name = "borrower_id")
    Long borrowerId   // Only store the foreign key value as a reference
) {}
