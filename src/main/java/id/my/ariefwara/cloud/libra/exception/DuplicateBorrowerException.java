package id.my.ariefwara.cloud.libra.exception;

public class DuplicateBorrowerException extends RuntimeException {
    public DuplicateBorrowerException(String email) {
        super("A borrower with email '" + email + "' already exists.");
    }
}
