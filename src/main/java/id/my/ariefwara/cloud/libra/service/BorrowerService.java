package id.my.ariefwara.cloud.libra.service;

import id.my.ariefwara.cloud.libra.dto.BorrowerDTO;
import id.my.ariefwara.cloud.libra.exception.DuplicateBorrowerException;
import id.my.ariefwara.cloud.libra.model.Borrower;
import id.my.ariefwara.cloud.libra.repository.BorrowerRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BorrowerService {
    private static final Logger logger = LoggerFactory.getLogger(BorrowerService.class);

    private final BorrowerRepository borrowerRepository;
    private final ModelMapper modelMapper;

    public BorrowerService(BorrowerRepository borrowerRepository, ModelMapper modelMapper) {
        this.borrowerRepository = borrowerRepository;
        this.modelMapper = modelMapper;
    }

    public BorrowerDTO registerBorrower(BorrowerDTO borrowerDTO) {
        logger.info("Attempting to register new borrower with email: {}", borrowerDTO.email());
        logger.debug("Checking for existing borrower with email: {}", borrowerDTO.email());

        Optional<Borrower> existingBorrower = borrowerRepository.findByEmail(borrowerDTO.email());
        if (existingBorrower.isPresent()) {
            logger.warn("Borrower with email '{}' already exists. Existing ID: {}", 
                        borrowerDTO.email(), existingBorrower.get().getBorrowerId());
            throw new DuplicateBorrowerException(borrowerDTO.email());
        }

        logger.debug("No existing borrower found with email: {}. Creating new borrower entry.", borrowerDTO.email());
        Borrower borrower = modelMapper.map(borrowerDTO, Borrower.class);

        logger.debug("Saving new borrower to the database.");
        Borrower savedBorrower = borrowerRepository.save(borrower);

        logger.info("Successfully registered new borrower with ID: {} and email: {}", 
                    savedBorrower.getBorrowerId(), savedBorrower.getEmail());

        logger.debug("Mapping saved borrower entity to DTO.");
        return modelMapper.map(savedBorrower, BorrowerDTO.class);
    }
}
