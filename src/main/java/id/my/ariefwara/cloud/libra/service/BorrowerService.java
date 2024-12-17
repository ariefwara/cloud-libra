package id.my.ariefwara.cloud.libra.service;

import id.my.ariefwara.cloud.libra.model.Borrower;
import id.my.ariefwara.cloud.libra.repository.BorrowerRepository;
import org.springframework.stereotype.Service;

@Service
public class BorrowerService {
    private final BorrowerRepository borrowerRepository;

    public BorrowerService(BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    public Borrower registerBorrower(Borrower borrower) {
        return borrowerRepository.save(borrower);
    }
}
