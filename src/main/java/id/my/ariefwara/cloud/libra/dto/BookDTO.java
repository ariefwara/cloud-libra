package id.my.ariefwara.cloud.libra.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record BookDTO(
    
    UUID bookId,

    @NotBlank(message = "ISBN is required")
    String isbn,

    @NotBlank(message = "Title is required")
    String title,

    @NotBlank(message = "Author is required")
    String author
) {}
