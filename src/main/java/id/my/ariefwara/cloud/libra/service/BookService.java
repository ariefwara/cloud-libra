package id.my.ariefwara.cloud.libra.service;

import id.my.ariefwara.cloud.libra.dto.BookDTO;
import id.my.ariefwara.cloud.libra.exception.BookNotFoundException;
import id.my.ariefwara.cloud.libra.exception.BookAlreadyBorrowedException;
import id.my.ariefwara.cloud.libra.exception.BookConflictException;
import id.my.ariefwara.cloud.libra.exception.BookNotBorrowedException;
import id.my.ariefwara.cloud.libra.model.Book;
import id.my.ariefwara.cloud.libra.repository.BookRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookDTO borrowBook(Long bookId, Long borrowerId) {
        logger.info("Attempting to borrow book with ID: {} for borrower ID: {}", bookId, borrowerId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    logger.warn("Book with ID: {} not found.", bookId);
                    return new BookNotFoundException(bookId);
                });

        if (book.borrowerId() != null) {
            logger.warn("Book with ID: {} is already borrowed by borrower ID: {}", bookId, book.borrowerId());
            throw new BookAlreadyBorrowedException(bookId);
        }

        Book updatedBook = new Book(book.bookId(), book.isbn(), book.title(), book.author(), borrowerId);
        Book savedBook = bookRepository.save(updatedBook);

        logger.info("Book with ID: {} successfully borrowed by borrower ID: {}", savedBook.bookId(), borrowerId);
        return new BookDTO(savedBook.bookId(), savedBook.isbn(), savedBook.title(), savedBook.author());
    }

    public BookDTO registerBook(BookDTO bookDTO) {
        logger.info("Attempting to register book with ISBN: {}", bookDTO.isbn());

        Optional<Book> existingBook = bookRepository.findFirstByIsbn(bookDTO.isbn());

        if (existingBook.isPresent()) {
            Book book = existingBook.get();
            if (!book.title().equals(bookDTO.title()) || !book.author().equals(bookDTO.author())) {
                logger.warn("Conflict detected for ISBN: {}. Existing title: {}, Existing author: {}. New title: {}, New author: {}",
                        bookDTO.isbn(), book.title(), book.author(), bookDTO.title(), bookDTO.author());
                throw new BookConflictException(
                        bookDTO.isbn(),
                        book.title(), book.author(),
                        bookDTO.title(), bookDTO.author()
                );
            }
            logger.info("Book with ISBN: {} already exists. Returning existing book details.", bookDTO.isbn());
            return new BookDTO(book.bookId(), book.isbn(), book.title(), book.author());
        }

        Book newBook = new Book(null, bookDTO.isbn(), bookDTO.title(), bookDTO.author(), null);
        Book savedBook = bookRepository.save(newBook);

        logger.info("Book with ISBN: {} successfully registered with ID: {}", savedBook.isbn(), savedBook.bookId());
        return new BookDTO(savedBook.bookId(), savedBook.isbn(), savedBook.title(), savedBook.author());
    }

    public List<BookDTO> getAllBooks() {
        logger.info("Fetching all books from the library.");
        List<BookDTO> books = bookRepository.findAll().stream()
                .map(book -> new BookDTO(book.bookId(), book.isbn(), book.title(), book.author()))
                .collect(Collectors.toList());
        logger.info("Total books fetched: {}", books.size());
        return books;
    }

    public BookDTO returnBook(Long bookId) {
        logger.info("Attempting to return book with ID: {}", bookId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    logger.warn("Book with ID: {} not found.", bookId);
                    return new BookNotFoundException(bookId);
                });

        if (book.borrowerId() == null) {
            logger.warn("Book with ID: {} is not currently borrowed.", bookId);
            throw new BookNotBorrowedException(bookId);
        }

        Book updatedBook = new Book(book.bookId(), book.isbn(), book.title(), book.author(), null);
        Book savedBook = bookRepository.save(updatedBook);

        logger.info("Book with ID: {} successfully returned.", savedBook.bookId());
        return new BookDTO(savedBook.bookId(), savedBook.isbn(), savedBook.title(), savedBook.author());
    }
}
