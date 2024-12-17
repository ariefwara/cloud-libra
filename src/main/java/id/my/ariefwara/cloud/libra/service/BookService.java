package id.my.ariefwara.cloud.libra.service;

import id.my.ariefwara.cloud.libra.model.Book;
import id.my.ariefwara.cloud.libra.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book registerBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book borrowBook(Long bookId, Long borrowerId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        if (book.borrowerId() != null) {
            throw new RuntimeException("Book is already borrowed");
        }
        Book updatedBook = new Book(book.bookId(), book.isbn(), book.title(), book.author(), borrowerId);
        return bookRepository.save(updatedBook);
    }

    public Book returnBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        Book updatedBook = new Book(book.bookId(), book.isbn(), book.title(), book.author(), null);
        return bookRepository.save(updatedBook);
    }
}
