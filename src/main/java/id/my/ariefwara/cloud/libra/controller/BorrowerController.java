package id.my.ariefwara.cloud.libra.controller;

import id.my.ariefwara.cloud.libra.dto.BorrowerDTO;
import id.my.ariefwara.cloud.libra.service.BorrowerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrowers")
public class BorrowerController {

    private final BorrowerService borrowerService;

    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    @PostMapping
    public ResponseEntity<BorrowerDTO> registerBorrower(@RequestBody BorrowerDTO borrowerDTO) {
        return ResponseEntity.ok(borrowerService.registerBorrower(borrowerDTO));
    }
}
