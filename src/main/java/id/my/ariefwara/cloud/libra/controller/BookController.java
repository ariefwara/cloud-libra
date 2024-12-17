package id.my.ariefwara.cloud.libra.controller;

import id.my.ariefwara.cloud.libra.model.Book;
import id.my.ariefwara.cloud.libra.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<Book> registerBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.registerBook(book));
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @PutMapping("/{bookId}/borrow/{borrowerId}")
    public ResponseEntity<Book> borrowBook(@PathVariable Long bookId, @PathVariable Long borrowerId) {
        return ResponseEntity.ok(bookService.borrowBook(bookId, borrowerId));
    }

    @PutMapping("/{bookId}/return")
    public ResponseEntity<Book> returnBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.returnBook(bookId));
    }
}
