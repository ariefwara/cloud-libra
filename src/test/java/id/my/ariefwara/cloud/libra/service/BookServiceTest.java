package id.my.ariefwara.cloud.libra.service;

import id.my.ariefwara.cloud.libra.dto.BookDTO;
import id.my.ariefwara.cloud.libra.exception.BookAlreadyBorrowedException;
import id.my.ariefwara.cloud.libra.exception.BookConflictException;
import id.my.ariefwara.cloud.libra.exception.BookNotBorrowedException;
import id.my.ariefwara.cloud.libra.model.Book;
import id.my.ariefwara.cloud.libra.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerBook_SuccessfulRegistration() {
        // Given
        BookDTO newBook = new BookDTO(null, "1234567890", "Clean Code", "Robert C. Martin");
        Book savedBook = new Book(1L, "1234567890", "Clean Code", "Robert C. Martin", null);

        when(bookRepository.findFirstByIsbn(newBook.isbn())).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        // When
        BookDTO result = bookService.registerBook(newBook);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.bookId());
        assertEquals("Clean Code", result.title());
        assertEquals("Robert C. Martin", result.author());

        verify(bookRepository, times(1)).findFirstByIsbn(newBook.isbn());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void registerBook_ConflictThrowsException() {
        // Given
        BookDTO newBook = new BookDTO(null, "1234567890", "Clean Code", "Robert C. Martin");
        Book existingBook = new Book(1L, "1234567890", "Another Title", "Another Author", null);

        when(bookRepository.findFirstByIsbn(newBook.isbn())).thenReturn(Optional.of(existingBook));

        // When & Then
        assertThrows(BookConflictException.class, () -> bookService.registerBook(newBook));

        verify(bookRepository, times(1)).findFirstByIsbn(newBook.isbn());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void borrowBook_Successful() {
        // Given
        Long bookId = 1L, borrowerId = 100L;
        Book book = new Book(bookId, "1234567890", "Clean Code", "Robert C. Martin", null);
        Book updatedBook = new Book(bookId, "1234567890", "Clean Code", "Robert C. Martin", borrowerId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        // When
        BookDTO result = bookService.borrowBook(bookId, borrowerId);

        // Then
        assertNotNull(result);
        assertEquals(borrowerId, updatedBook.getBorrowerId());

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void borrowBook_AlreadyBorrowedThrowsException() {
        // Given
        Long bookId = 1L, borrowerId = 100L;
        Book alreadyBorrowedBook = new Book(bookId, "1234567890", "Clean Code", "Robert C. Martin", 999L);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(alreadyBorrowedBook));

        // When & Then
        assertThrows(BookAlreadyBorrowedException.class, () -> bookService.borrowBook(bookId, borrowerId));

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void returnBook_Successful() {
        // Given
        Long bookId = 1L;
        Book borrowedBook = new Book(bookId, "1234567890", "Clean Code", "Robert C. Martin", 100L);
        Book returnedBook = new Book(bookId, "1234567890", "Clean Code", "Robert C. Martin", null);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(borrowedBook));
        when(bookRepository.save(any(Book.class))).thenReturn(returnedBook);

        // When
        BookDTO result = bookService.returnBook(bookId);

        // Then
        assertNotNull(result);
        assertNull(returnedBook.getBorrowerId());

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void returnBook_NotBorrowedThrowsException() {
        // Given
        Long bookId = 1L;
        Book bookNotBorrowed = new Book(bookId, "1234567890", "Clean Code", "Robert C. Martin", null);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookNotBorrowed));

        // When & Then
        assertThrows(BookNotBorrowedException.class, () -> bookService.returnBook(bookId));

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void getAllBooks_ReturnsBooksList() {
        // Given
        List<Book> books = Arrays.asList(
                new Book(1L, "1234567890", "Clean Code", "Robert C. Martin", null),
                new Book(2L, "9876543210", "Clean Architecture", "Robert C. Martin", null)
        );

        when(bookRepository.findAll()).thenReturn(books);

        // When
        List<BookDTO> result = bookService.getAllBooks();

        // Then
        assertEquals(2, result.size());
        assertEquals("Clean Code", result.get(0).title());
        assertEquals("Clean Architecture", result.get(1).title());

        verify(bookRepository, times(1)).findAll();
    }
}
