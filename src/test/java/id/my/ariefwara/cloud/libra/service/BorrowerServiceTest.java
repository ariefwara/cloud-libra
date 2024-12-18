package id.my.ariefwara.cloud.libra.service;

import id.my.ariefwara.cloud.libra.dto.BorrowerDTO;
import id.my.ariefwara.cloud.libra.exception.DuplicateBorrowerException;
import id.my.ariefwara.cloud.libra.model.Borrower;
import id.my.ariefwara.cloud.libra.repository.BorrowerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorrowerServiceTest {

    @Mock
    private BorrowerRepository borrowerRepository;

    @InjectMocks
    private BorrowerService borrowerService;

    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        modelMapper = new ModelMapper(); // Initialize the ModelMapper
        borrowerService = new BorrowerService(borrowerRepository, modelMapper);
    }

    @Test
    void registerBorrower_SuccessfulRegistration() {
        // Given
        BorrowerDTO newBorrower = new BorrowerDTO(null, "John Doe", "john.doe@example.com");
        Borrower savedBorrower = new Borrower(java.util.UUID.randomUUID(), "John Doe", "john.doe@example.com");

        when(borrowerRepository.findByEmail(newBorrower.getEmail())).thenReturn(Optional.empty());
        when(borrowerRepository.save(any(Borrower.class))).thenReturn(savedBorrower);

        // When
        BorrowerDTO result = borrowerService.registerBorrower(newBorrower);

        // Then
        assertNotNull(result);
        assertEquals(savedBorrower.getBorrowerId(), result.getBorrowerId());
        assertEquals(savedBorrower.getName(), result.getName());
        assertEquals(savedBorrower.getEmail(), result.getEmail());

        verify(borrowerRepository, times(1)).findByEmail(newBorrower.getEmail());
        verify(borrowerRepository, times(1)).save(any(Borrower.class));
    }

    @Test
    void registerBorrower_ThrowsDuplicateBorrowerException() {
        // Given
        BorrowerDTO newBorrower = new BorrowerDTO(null, "John Doe", "john.doe@example.com");
        Borrower existingBorrower = new Borrower(java.util.UUID.randomUUID(), "John Doe", "john.doe@example.com");

        when(borrowerRepository.findByEmail(newBorrower.getEmail())).thenReturn(Optional.of(existingBorrower));

        // When & Then
        assertThrows(DuplicateBorrowerException.class, () -> borrowerService.registerBorrower(newBorrower));

        verify(borrowerRepository, times(1)).findByEmail(newBorrower.getEmail());
        verify(borrowerRepository, never()).save(any(Borrower.class));
    }
}
