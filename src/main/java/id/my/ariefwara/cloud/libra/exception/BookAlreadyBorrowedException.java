package id.my.ariefwara.cloud.libra.exception;

import java.util.UUID;

public class BookAlreadyBorrowedException extends RuntimeException {
    public BookAlreadyBorrowedException(UUID bookId) {
        super("Book with ID " + bookId + " is already borrowed");
    }
}
