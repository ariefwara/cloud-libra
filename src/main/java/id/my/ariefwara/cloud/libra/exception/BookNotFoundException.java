package id.my.ariefwara.cloud.libra.exception;

import java.util.UUID;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(UUID bookId) {
        super("Book with ID " + bookId + " not found");
    }
}
