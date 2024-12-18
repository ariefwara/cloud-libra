package id.my.ariefwara.cloud.libra.controller;

import id.my.ariefwara.cloud.libra.dto.BookDTO;
import id.my.ariefwara.cloud.libra.model.Book;
import id.my.ariefwara.cloud.libra.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Books", description = "API for managing books in the library")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Register a new book", description = "Adds a new book to the library system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Book registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid book details provided")
    })
    @PostMapping
    public ResponseEntity<BookDTO> registerBook(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Book details for registration") 
            @Valid @RequestBody BookDTO bookDTO) {
        BookDTO savedBook = bookService.registerBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @Operation(summary = "Get all books (Paginated)", description = "Retrieves a paginated list of books in the library.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid page or size parameter")
    })
    @GetMapping
    public ResponseEntity<Page<Book>> getAllBooks(
            @Parameter(description = "Page number (0-based)", example = "0") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of books per page", example = "10") 
            @RequestParam(defaultValue = "10") int size) {

        Page<Book> books = bookService.getAllBooks(page, size);
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Borrow a book", description = "Marks a book as borrowed by a specific borrower.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book borrowed successfully"),
        @ApiResponse(responseCode = "404", description = "Book not found"),
        @ApiResponse(responseCode = "409", description = "Book already borrowed")
    })
    @PutMapping("/{bookId}/borrow/{borrowerId}")
    public ResponseEntity<BookDTO> borrowBook(
            @Parameter(description = "ID of the book to borrow") @PathVariable UUID bookId,
            @Parameter(description = "ID of the borrower") @PathVariable UUID borrowerId) {
        BookDTO updatedBook = bookService.borrowBook(bookId, borrowerId);
        return ResponseEntity.ok(updatedBook);
    }

    @Operation(summary = "Return a book", description = "Marks a borrowed book as returned.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book returned successfully"),
        @ApiResponse(responseCode = "404", description = "Book not found"),
        @ApiResponse(responseCode = "400", description = "Book was not borrowed")
    })
    @PutMapping("/{bookId}/return")
    public ResponseEntity<BookDTO> returnBook(
            @Parameter(description = "ID of the book to return") @PathVariable UUID bookId) {
        BookDTO updatedBook = bookService.returnBook(bookId);
        return ResponseEntity.ok(updatedBook);
    }
}
