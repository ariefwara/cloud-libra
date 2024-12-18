package id.my.ariefwara.cloud.libra.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "book_id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID bookId;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "borrower_id")
    private UUID borrowerId;

    public Book() {}

    public Book(UUID bookId, String isbn, String title, String author, UUID borrowerId) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.borrowerId = borrowerId;
    }

    // Getters and Setters
    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public UUID getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(UUID borrowerId) {
        this.borrowerId = borrowerId;
    }
    
}
