package id.my.ariefwara.cloud.libra.repository;

import id.my.ariefwara.cloud.libra.model.Borrower;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, Long> {
    Optional<Borrower> findByEmail(String email);
}
