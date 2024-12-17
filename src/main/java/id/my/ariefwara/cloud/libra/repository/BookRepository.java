package id.my.ariefwara.cloud.libra.repository;

import id.my.ariefwara.cloud.libra.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByBorrowerIdIsNull(); // Find books not borrowed
    List<Book> findByBorrowerId(Long borrowerId); // Find books borrowed by a borrower
}