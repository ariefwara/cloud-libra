package id.my.ariefwara.cloud.libra.exception;

import java.util.UUID;

public class BookNotBorrowedException extends RuntimeException {
    public BookNotBorrowedException(UUID bookId) {
        super("Book with ID '" + bookId + "' is not currently borrowed.");
    }
}
