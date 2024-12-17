package id.my.ariefwara.cloud.libra.service;

import id.my.ariefwara.cloud.libra.dto.BorrowerDTO;
import id.my.ariefwara.cloud.libra.model.Borrower;
import id.my.ariefwara.cloud.libra.repository.BorrowerRepository;
import org.springframework.stereotype.Service;

@Service
public class BorrowerService {
    private final BorrowerRepository borrowerRepository;

    public BorrowerService(BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    public BorrowerDTO registerBorrower(BorrowerDTO borrowerDTO) {
        Borrower borrower = new Borrower(null, borrowerDTO.name(), borrowerDTO.email());
        Borrower savedBorrower = borrowerRepository.save(borrower);
        return new BorrowerDTO(savedBorrower.borrowerId(), savedBorrower.name(), savedBorrower.email());
    }
}
