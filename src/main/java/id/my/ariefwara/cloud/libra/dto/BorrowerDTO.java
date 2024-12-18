package id.my.ariefwara.cloud.libra.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record BorrowerDTO(
    Long borrowerId,

    @NotBlank(message = "Name is required")
    String name,

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be in a valid format")
    String email
) {}
