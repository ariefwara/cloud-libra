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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BorrowerServiceTest {

    @Mock
    private BorrowerRepository borrowerRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BorrowerService borrowerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerBorrower_SuccessfulRegistration() {
        // Given
        BorrowerDTO newBorrowerDTO = new BorrowerDTO(null, "John Doe", "john.doe@example.com");
        Borrower borrowerEntity = new Borrower(null, "John Doe", "john.doe@example.com");
        Borrower savedBorrowerEntity = new Borrower(UUID.randomUUID(), "John Doe", "john.doe@example.com");
        BorrowerDTO savedBorrowerDTO = new BorrowerDTO(
                savedBorrowerEntity.getBorrowerId(), "John Doe", "john.doe@example.com");

        when(borrowerRepository.findByEmail(newBorrowerDTO.getEmail())).thenReturn(Optional.empty());
        when(modelMapper.map(newBorrowerDTO, Borrower.class)).thenReturn(borrowerEntity);
        when(borrowerRepository.save(borrowerEntity)).thenReturn(savedBorrowerEntity);
        when(modelMapper.map(savedBorrowerEntity, BorrowerDTO.class)).thenReturn(savedBorrowerDTO);

        // When
        BorrowerDTO result = borrowerService.registerBorrower(newBorrowerDTO);

        // Then
        assertNotNull(result);
        assertEquals(savedBorrowerDTO.getBorrowerId(), result.getBorrowerId());
        assertEquals(savedBorrowerDTO.getName(), result.getName());
        assertEquals(savedBorrowerDTO.getEmail(), result.getEmail());

        verify(borrowerRepository, times(1)).findByEmail(newBorrowerDTO.getEmail());
        verify(borrowerRepository, times(1)).save(any(Borrower.class));
        verify(modelMapper, times(2)).map(any(), any());
    }

    @Test
    void registerBorrower_ThrowsDuplicateBorrowerException() {
        // Given
        BorrowerDTO newBorrowerDTO = new BorrowerDTO(null, "John Doe", "john.doe@example.com");
        Borrower existingBorrower = new Borrower(UUID.randomUUID(), "John Doe", "john.doe@example.com");

        when(borrowerRepository.findByEmail(newBorrowerDTO.getEmail())).thenReturn(Optional.of(existingBorrower));

        // When & Then
        DuplicateBorrowerException exception = assertThrows(
                DuplicateBorrowerException.class,
                () -> borrowerService.registerBorrower(newBorrowerDTO)
        );

        assertTrue(exception.getMessage().contains("john.doe@example.com"));
        verify(borrowerRepository, times(1)).findByEmail(newBorrowerDTO.getEmail());
        verify(borrowerRepository, never()).save(any(Borrower.class));
        verifyNoInteractions(modelMapper);
    }

}
