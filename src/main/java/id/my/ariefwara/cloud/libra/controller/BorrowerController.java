package id.my.ariefwara.cloud.libra.controller;

import id.my.ariefwara.cloud.libra.dto.BorrowerDTO;
import id.my.ariefwara.cloud.libra.service.BorrowerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Borrowers", description = "API for managing library borrowers")
@RestController
@RequestMapping("/api/borrowers")
public class BorrowerController {

    private final BorrowerService borrowerService;

    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    @Operation(
        summary = "Register a new borrower", 
        description = "Registers a new borrower to the library system with their name and email."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Borrower successfully registered"),
        @ApiResponse(responseCode = "400", description = "Invalid borrower details provided")
    })
    @PostMapping
    public ResponseEntity<BorrowerDTO> registerBorrower(
            @RequestBody(description = "Details of the borrower to register")
            @Valid @org.springframework.web.bind.annotation.RequestBody BorrowerDTO borrowerDTO) {
        
        BorrowerDTO savedBorrower = borrowerService.registerBorrower(borrowerDTO);
        return ResponseEntity.ok(savedBorrower);
    }
}
