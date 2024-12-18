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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorrowerServiceTest {

    @Mock
    private BorrowerRepository borrowerRepository;

    @InjectMocks
    private BorrowerService borrowerService;

    @BeforeEach
    void setUp() {
        // Initialize the mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerBorrower_SuccessfulRegistration() {
        // Given
        BorrowerDTO newBorrower = new BorrowerDTO(null, "John Doe", "john.doe@example.com");
        Borrower savedEntity = new Borrower(1L, "John Doe", "john.doe@example.com");

        // Simulate repository behavior
        when(borrowerRepository.findByEmail(newBorrower.email())).thenReturn(Optional.empty());
        when(borrowerRepository.save(any(Borrower.class))).thenReturn(savedEntity);

        // When
        BorrowerDTO result = borrowerService.registerBorrower(newBorrower);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.borrowerId());
        assertEquals("John Doe", result.name());
        assertEquals("john.doe@example.com", result.email());

        // Verify interactions
        verify(borrowerRepository, times(1)).findByEmail(newBorrower.email());
        verify(borrowerRepository, times(1)).save(any(Borrower.class));
    }

    @Test
    void registerBorrower_DuplicateEmailThrowsException() {
        // Given
        BorrowerDTO newBorrower = new BorrowerDTO(null, "John Doe", "john.doe@example.com");
        Borrower existingBorrower = new Borrower(1L, "John Doe", "john.doe@example.com");

        // Simulate repository behavior
        when(borrowerRepository.findByEmail(newBorrower.email())).thenReturn(Optional.of(existingBorrower));

        // When & Then
        DuplicateBorrowerException exception = assertThrows(
                DuplicateBorrowerException.class,
                () -> borrowerService.registerBorrower(newBorrower)
        );

        assertEquals("A borrower with email 'john.doe@example.com' already exists.", exception.getMessage());

        // Verify interactions
        verify(borrowerRepository, times(1)).findByEmail(newBorrower.email());
        verify(borrowerRepository, never()).save(any(Borrower.class));
    }
}
