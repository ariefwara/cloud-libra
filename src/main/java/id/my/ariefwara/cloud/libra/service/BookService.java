package id.my.ariefwara.cloud.libra.service;

import id.my.ariefwara.cloud.libra.dto.BookDTO;
import id.my.ariefwara.cloud.libra.exception.BookNotFoundException;
import id.my.ariefwara.cloud.libra.exception.BookAlreadyBorrowedException;
import id.my.ariefwara.cloud.libra.exception.BookConflictException;
import id.my.ariefwara.cloud.libra.exception.BookNotBorrowedException;
import id.my.ariefwara.cloud.libra.model.Book;
import id.my.ariefwara.cloud.libra.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public BookService(BookRepository bookRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    public BookDTO borrowBook(UUID bookId, UUID borrowerId) {
        logger.info("Attempting to borrow book with ID: {} for borrower ID: {}", bookId, borrowerId);
        logger.debug("Fetching book with ID: {} using pessimistic lock.", bookId);

        Book book = bookRepository.findByIdForUpdate(bookId)
                .orElseThrow(() -> {
                    logger.warn("Book with ID: {} not found.", bookId);
                    return new BookNotFoundException(bookId);
                });

        logger.debug("Book with ID: {} retrieved. Current borrower ID: {}", bookId, book.getBorrowerId());

        if (book.getBorrowerId() != null) {
            logger.warn("Book with ID: {} is already borrowed by borrower ID: {}", bookId, book.getBorrowerId());
            throw new BookAlreadyBorrowedException(bookId);
        }

        logger.debug("Assigning borrower ID: {} to book with ID: {}", borrowerId, bookId);
        book.setBorrowerId(borrowerId);

        logger.debug("Saving updated book with ID: {}.", bookId);
        Book savedBook = bookRepository.save(book);

        logger.info("Book with ID: {} successfully borrowed by borrower ID: {}", bookId, borrowerId);
        return modelMapper.map(savedBook, BookDTO.class);
    }

    public BookDTO registerBook(BookDTO bookDTO) {
        logger.info("Attempting to register book with ISBN: {}", bookDTO.getIsbn());
        logger.debug("Checking for existing book with ISBN: {}", bookDTO.getIsbn());

        Optional<Book> existingBook = bookRepository.findFirstByIsbn(bookDTO.getIsbn());

        if (existingBook.isPresent()) {
            logger.debug("Book with ISBN: {} already exists. Checking for conflicts.", bookDTO.getIsbn());
            Book book = existingBook.get();
            if (!Objects.equals(book.getTitle(), bookDTO.getTitle()) || !Objects.equals(book.getAuthor(), bookDTO.getAuthor())) {
                logger.warn("Conflict detected for ISBN: {}. Existing: [{} - {}], New: [{} - {}]",
                        bookDTO.getIsbn(), book.getTitle(), book.getAuthor(), bookDTO.getTitle(), bookDTO.getAuthor());
                throw new BookConflictException(
                        bookDTO.getIsbn(), book.getTitle(), book.getAuthor(), bookDTO.getTitle(), bookDTO.getAuthor()
                );
            }
            logger.info("Book with ISBN: {} already exists. Returning existing book details.", bookDTO.getIsbn());
            return modelMapper.map(book, BookDTO.class);
        }

        logger.debug("No conflicts found. Creating a new book entry.");
        Book newBook = modelMapper.map(bookDTO, Book.class);
        Book savedBook = bookRepository.save(newBook);

        logger.info("Book with ISBN: {} successfully registered with ID: {}", savedBook.getIsbn(), savedBook.getBookId());
        return modelMapper.map(savedBook, BookDTO.class);
    }

    public List<BookDTO> getAllBooks() {
        logger.info("Fetching all books from the library.");
        logger.debug("Executing findAll() on repository.");
        List<BookDTO> books = bookRepository.findAll().stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());

        logger.info("Total books fetched: {}", books.size());
        return books;
    }

    public BookDTO returnBook(UUID bookId) {
        logger.info("Attempting to return book with ID: {}", bookId);
        logger.debug("Fetching book with ID: {}.", bookId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    logger.warn("Book with ID: {} not found.", bookId);
                    return new BookNotFoundException(bookId);
                });

        logger.debug("Checking if book with ID: {} is currently borrowed.", bookId);
        if (book.getBorrowerId() == null) {
            logger.warn("Book with ID: {} is not currently borrowed.", bookId);
            throw new BookNotBorrowedException(bookId);
        }

        logger.debug("Removing borrower ID from book with ID: {}", bookId);
        book.setBorrowerId(null);
        Book savedBook = bookRepository.save(book);

        logger.info("Book with ID: {} successfully returned.", savedBook.getBookId());
        return modelMapper.map(savedBook, BookDTO.class);
    }

    public Page<BookDTO> getAllBooks(int page, int size) {
        logger.info("Fetching paginated books from the library. Page: {}, Size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        logger.debug("Executing findAll(Pageable) on repository.");

        Page<BookDTO> bookDTOPage = bookRepository.findAll(pageable)
                .map(book -> modelMapper.map(book, BookDTO.class));

        logger.info("Total books fetched in this page: {}", bookDTOPage.getContent().size());
        return bookDTOPage;
    }
}
