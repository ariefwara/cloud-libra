package id.my.ariefwara.cloud.libra.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long bookId) {
        super("Book with ID " + bookId + " not found");
    }
}
