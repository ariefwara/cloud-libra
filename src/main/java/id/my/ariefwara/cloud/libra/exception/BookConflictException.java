package id.my.ariefwara.cloud.libra.exception;

public class BookConflictException extends RuntimeException {
    public BookConflictException(String isbn, String existingTitle, String existingAuthor, String newTitle, String newAuthor) {
        super("A book with ISBN '" + isbn + "' already exists but has different details. " +
              "Existing Book: Title = '" + existingTitle + "', Author = '" + existingAuthor + "'. " +
              "New Book: Title = '" + newTitle + "', Author = '" + newAuthor + "'.");
    }
}
