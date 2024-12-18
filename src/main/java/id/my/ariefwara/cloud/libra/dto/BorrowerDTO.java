package id.my.ariefwara.cloud.libra.dto;

import java.util.UUID;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class BorrowerDTO {

    private UUID borrowerId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be in a valid format")
    private String email;

    public BorrowerDTO() {}

    public BorrowerDTO(UUID borrowerId, String name, String email) {
        this.borrowerId = borrowerId;
        this.name = name;
        this.email = email;
    }

    public UUID getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(UUID borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
