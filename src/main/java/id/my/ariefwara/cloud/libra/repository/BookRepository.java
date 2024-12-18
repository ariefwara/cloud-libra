package id.my.ariefwara.cloud.libra.repository;

import id.my.ariefwara.cloud.libra.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findFirstByIsbn(String isbn);
}