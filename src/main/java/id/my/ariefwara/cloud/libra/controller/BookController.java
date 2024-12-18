package id.my.ariefwara.cloud.libra.controller;

import id.my.ariefwara.cloud.libra.dto.BookDTO;
import id.my.ariefwara.cloud.libra.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Books", description = "API for managing books in the library")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Register a new book", description = "Adds a new book to the library system.")
    @PostMapping
    public ResponseEntity<BookDTO> registerBook(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Book details for registration") 
            @Valid @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.registerBook(bookDTO));
    }

    @Operation(summary = "Get all books", description = "Retrieves a list of all books in the library.")
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @Operation(summary = "Borrow a book", description = "Marks a book as borrowed by a specific borrower.")
    @PutMapping("/{bookId}/borrow/{borrowerId}")
    public ResponseEntity<BookDTO> borrowBook(
            @Parameter(description = "ID of the book to borrow") @PathVariable Long bookId,
            @Parameter(description = "ID of the borrower") @PathVariable Long borrowerId) {
        return ResponseEntity.ok(bookService.borrowBook(bookId, borrowerId));
    }

    @Operation(summary = "Return a book", description = "Marks a borrowed book as returned.")
    @PutMapping("/{bookId}/return")
    public ResponseEntity<BookDTO> returnBook(
            @Parameter(description = "ID of the book to return") @PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.returnBook(bookId));
    }
}
