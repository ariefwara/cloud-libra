package id.my.ariefwara.cloud.libra.service;

import id.my.ariefwara.cloud.libra.dto.BookDTO;
import id.my.ariefwara.cloud.libra.exception.BookAlreadyBorrowedException;
import id.my.ariefwara.cloud.libra.exception.BookConflictException;
import id.my.ariefwara.cloud.libra.exception.BookNotBorrowedException;
import id.my.ariefwara.cloud.libra.exception.BookNotFoundException;
import id.my.ariefwara.cloud.libra.model.Book;
import id.my.ariefwara.cloud.libra.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void borrowBook_Successful() {
        UUID bookId = UUID.randomUUID(), borrowerId = UUID.randomUUID();
        Book book = new Book(bookId, "1234567890", "Title", "Author", null);
        Book updatedBook = new Book(bookId, "1234567890", "Title", "Author", borrowerId);
        BookDTO bookDTO = new BookDTO(bookId, "1234567890", "Title", "Author");

        when(bookRepository.findByIdForUpdate(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(updatedBook);
        when(modelMapper.map(updatedBook, BookDTO.class)).thenReturn(bookDTO);

        BookDTO result = bookService.borrowBook(bookId, borrowerId);

        assertNotNull(result);
        assertEquals(borrowerId, updatedBook.getBorrowerId());
        verify(bookRepository).findByIdForUpdate(bookId);
        verify(bookRepository).save(book);
    }

    @Test
    void borrowBook_NotFoundThrowsException() {
        UUID bookId = UUID.randomUUID(), borrowerId = UUID.randomUUID();

        when(bookRepository.findByIdForUpdate(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.borrowBook(bookId, borrowerId));
        verify(bookRepository).findByIdForUpdate(bookId);
    }

    @Test
    void borrowBook_AlreadyBorrowedThrowsException() {
        UUID bookId = UUID.randomUUID(), borrowerId = UUID.randomUUID();
        Book alreadyBorrowedBook = new Book(bookId, "1234567890", "Title", "Author", UUID.randomUUID());

        when(bookRepository.findByIdForUpdate(bookId)).thenReturn(Optional.of(alreadyBorrowedBook));

        assertThrows(BookAlreadyBorrowedException.class, () -> bookService.borrowBook(bookId, borrowerId));
        verify(bookRepository).findByIdForUpdate(bookId);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void registerBook_Successful() {
        BookDTO bookDTO = new BookDTO(null, "1234567890", "Title", "Author");
        Book book = new Book(null, "1234567890", "Title", "Author", null);
        Book savedBook = new Book(UUID.randomUUID(), "1234567890", "Title", "Author", null);
        BookDTO savedBookDTO = new BookDTO(savedBook.getBookId(), savedBook.getIsbn(), savedBook.getTitle(), savedBook.getAuthor());

        when(bookRepository.findFirstByIsbn(bookDTO.getIsbn())).thenReturn(Optional.empty());
        when(modelMapper.map(bookDTO, Book.class)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(modelMapper.map(savedBook, BookDTO.class)).thenReturn(savedBookDTO);

        BookDTO result = bookService.registerBook(bookDTO);

        assertNotNull(result);
        assertEquals(savedBookDTO.getBookId(), result.getBookId());
        verify(bookRepository).save(book);
    }

    @Test
    void registerBook_ConflictThrowsException() {
        BookDTO bookDTO = new BookDTO(null, "1234567890", "Title", "Author");
        Book existingBook = new Book(UUID.randomUUID(), "1234567890", "Different Title", "Different Author", null);

        when(bookRepository.findFirstByIsbn(bookDTO.getIsbn())).thenReturn(Optional.of(existingBook));

        assertThrows(BookConflictException.class, () -> bookService.registerBook(bookDTO));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void getAllBooks_ReturnsBooksList() {
        List<Book> books = Arrays.asList(
                new Book(UUID.randomUUID(), "1234567890", "Title1", "Author1", null),
                new Book(UUID.randomUUID(), "9876543210", "Title2", "Author2", null)
        );

        when(bookRepository.findAll()).thenReturn(books);
        when(modelMapper.map(any(Book.class), eq(BookDTO.class)))
                .thenAnswer(invocation -> {
                    Book book = invocation.getArgument(0);
                    return new BookDTO(book.getBookId(), book.getIsbn(), book.getTitle(), book.getAuthor());
                });

        List<BookDTO> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        verify(bookRepository).findAll();
    }

    @Test
    void returnBook_Successful() {
        UUID bookId = UUID.randomUUID();
        Book borrowedBook = new Book(bookId, "1234567890", "Title", "Author", UUID.randomUUID());
        Book returnedBook = new Book(bookId, "1234567890", "Title", "Author", null);
        BookDTO bookDTO = new BookDTO(bookId, "1234567890", "Title", "Author");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(borrowedBook));
        when(bookRepository.save(borrowedBook)).thenReturn(returnedBook);
        when(modelMapper.map(returnedBook, BookDTO.class)).thenReturn(bookDTO);

        BookDTO result = bookService.returnBook(bookId);

        assertNotNull(result);
        assertNull(returnedBook.getBorrowerId());
        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(borrowedBook);
    }

    @Test
    void returnBook_NotBorrowedThrowsException() {
        UUID bookId = UUID.randomUUID();
        Book notBorrowedBook = new Book(bookId, "1234567890", "Title", "Author", null);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(notBorrowedBook));

        assertThrows(BookNotBorrowedException.class, () -> bookService.returnBook(bookId));
        verify(bookRepository).findById(bookId);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void getAllBooksPaginated_ReturnsBooksPage() {
        List<Book> books = Arrays.asList(
                new Book(UUID.randomUUID(), "1234567890", "Title1", "Author1", null),
                new Book(UUID.randomUUID(), "9876543210", "Title2", "Author2", null)
        );
        Page<Book> page = new PageImpl<>(books);

        when(bookRepository.findAll(PageRequest.of(0, 2))).thenReturn(page);
        when(modelMapper.map(any(Book.class), eq(BookDTO.class)))
                .thenAnswer(invocation -> {
                    Book book = invocation.getArgument(0);
                    return new BookDTO(book.getBookId(), book.getIsbn(), book.getTitle(), book.getAuthor());
                });

        Page<BookDTO> result = bookService.getAllBooks(0, 2);

        assertEquals(2, result.getContent().size());
        verify(bookRepository).findAll(PageRequest.of(0, 2));
    }
}
