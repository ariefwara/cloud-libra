package id.my.ariefwara.cloud.libra.service;

import id.my.ariefwara.cloud.libra.dto.BookDTO;
import id.my.ariefwara.cloud.libra.exception.BookNotFoundException;
import id.my.ariefwara.cloud.libra.exception.BookAlreadyBorrowedException;
import id.my.ariefwara.cloud.libra.model.Book;
import id.my.ariefwara.cloud.libra.repository.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookDTO borrowBook(Long bookId, Long borrowerId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        if (book.borrowerId() != null) {
            throw new BookAlreadyBorrowedException(bookId);
        }
        Book updatedBook = new Book(book.bookId(), book.isbn(), book.title(), book.author(), borrowerId);
        Book savedBook = bookRepository.save(updatedBook);
        return new BookDTO(savedBook.bookId(), savedBook.isbn(), savedBook.title(), savedBook.author());
    }

    public BookDTO registerBook(BookDTO bookDTO) {
        Book book = new Book(null, bookDTO.isbn(), bookDTO.title(), bookDTO.author(), null);
        Book savedBook = bookRepository.save(book);
        return new BookDTO(savedBook.bookId(), savedBook.isbn(), savedBook.title(), savedBook.author());
    }

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(book -> new BookDTO(book.bookId(), book.isbn(), book.title(), book.author()))
                .collect(Collectors.toList());
    }

    public BookDTO returnBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        Book updatedBook = new Book(book.bookId(), book.isbn(), book.title(), book.author(), null);
        Book savedBook = bookRepository.save(updatedBook);
        return new BookDTO(savedBook.bookId(), savedBook.isbn(), savedBook.title(), savedBook.author());
    }
}
