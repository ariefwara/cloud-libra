package id.my.ariefwara.cloud.libra.exception;

public class BookNotBorrowedException extends RuntimeException {
    public BookNotBorrowedException(Long bookId) {
        super("Book with ID '" + bookId + "' is not currently borrowed.");
    }
}
