package id.my.ariefwara.cloud.libra.exception;

public class BookAlreadyBorrowedException extends RuntimeException {
    public BookAlreadyBorrowedException(Long bookId) {
        super("Book with ID " + bookId + " is already borrowed");
    }
}
