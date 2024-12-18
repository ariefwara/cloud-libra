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
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookDTO borrowBook(UUID bookId, UUID borrowerId) {
        logger.info("Attempting to borrow book with ID: {} for borrower ID: {}", bookId, borrowerId);
    
        logger.debug("Fetching book with ID: {} from repository with pessimistic lock.", bookId);
        Book book = bookRepository.findByIdForUpdate(bookId).orElseThrow(() -> {
            logger.warn("Book with ID: {} not found.", bookId);
            return new BookNotFoundException(bookId);
        });
        logger.debug("Successfully fetched book with ID: {}. Current borrower ID: {}", bookId, book.getBorrowerId());
    
        if (book.getBorrowerId() != null) {
            logger.warn("Book with ID: {} is already borrowed by borrower ID: {}", bookId, book.getBorrowerId());
            throw new BookAlreadyBorrowedException(bookId);
        }
        logger.debug("Book with ID: {} is available for borrowing.", bookId);
    
        logger.debug("Assigning borrower ID: {} to book with ID: {}", borrowerId, bookId);
        book.setBorrowerId(borrowerId);
    
        logger.debug("Saving updated book with ID: {} to repository.", bookId);
        Book savedBook = bookRepository.save(book);
        logger.debug("Successfully saved book with ID: {}. New borrower ID: {}", savedBook.getBookId(), savedBook.getBorrowerId());
    
        logger.info("Book with ID: {} successfully borrowed by borrower ID: {}", bookId, borrowerId);
        return new BookDTO(savedBook.getBookId(), savedBook.getIsbn(), savedBook.getTitle(), savedBook.getAuthor());
    }
    
    

    public BookDTO registerBook(BookDTO bookDTO) {
        logger.info("Attempting to register book with ISBN: {}", bookDTO.isbn());

        Optional<Book> existingBook = bookRepository.findFirstByIsbn(bookDTO.isbn());

        if (existingBook.isPresent()) {
            Book book = existingBook.get();
            logger.debug("Existing book found with ISBN: {}. Checking for title and author conflicts.", bookDTO.isbn());
            if (!book.getTitle().equals(bookDTO.title()) || !book.getAuthor().equals(bookDTO.author())) {
                logger.warn("Conflict detected for ISBN: {}. Existing title: {}, Existing author: {}. New title: {}, New author: {}",
                        bookDTO.isbn(), book.getTitle(), book.getAuthor(), bookDTO.title(), bookDTO.author());
                throw new BookConflictException(
                        bookDTO.isbn(),
                        book.getTitle(), book.getAuthor(),
                        bookDTO.title(), bookDTO.author()
                );
            }
            logger.info("Book with ISBN: {} already exists. Returning existing book details.", bookDTO.isbn());
            return new BookDTO(book.getBookId(), book.getIsbn(), book.getTitle(), book.getAuthor());
        }

        logger.debug("No existing book found with ISBN: {}. Creating new book entry.", bookDTO.isbn());
        Book newBook = new Book(null, bookDTO.isbn(), bookDTO.title(), bookDTO.author(), null);
        Book savedBook = bookRepository.save(newBook);

        logger.info("Book with ISBN: {} successfully registered with ID: {}", savedBook.getIsbn(), savedBook.getBookId());
        return new BookDTO(savedBook.getBookId(), savedBook.getIsbn(), savedBook.getTitle(), savedBook.getAuthor());
    }

    public List<BookDTO> getAllBooks() {
        logger.info("Fetching all books from the library.");
        List<BookDTO> books = bookRepository.findAll().stream()
                .map(book -> new BookDTO(book.getBookId(), book.getIsbn(), book.getTitle(), book.getAuthor()))
                .collect(Collectors.toList());
        logger.info("Total books fetched: {}", books.size());
        return books;
    }

    public BookDTO returnBook(UUID bookId) {
        logger.info("Attempting to return book with ID: {}", bookId);
        logger.debug("Fetching book with ID: {} from repository.", bookId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    logger.warn("Book with ID: {} not found.", bookId);
                    return new BookNotFoundException(bookId);
                });

        logger.debug("Book with ID: {} found. Checking if it is currently borrowed.", bookId);
        if (book.getBorrowerId() == null) {
            logger.warn("Book with ID: {} is not currently borrowed.", bookId);
            throw new BookNotBorrowedException(bookId);
        }

        logger.debug("Book with ID: {} is borrowed. Removing borrower ID.", bookId);
        book.setBorrowerId(null);
        Book savedBook = bookRepository.save(book);

        logger.info("Book with ID: {} successfully returned.", savedBook.getBookId());
        return new BookDTO(savedBook.getBookId(), savedBook.getIsbn(), savedBook.getTitle(), savedBook.getAuthor());
    }

    public Page<Book> getAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findAll(pageable);
    }
}
