package id.my.ariefwara.cloud.libra.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotBlank;

public class BookDTO {

    private UUID bookId;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    public BookDTO() {}

    public BookDTO(UUID bookId, String isbn, String title, String author) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }

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
}
