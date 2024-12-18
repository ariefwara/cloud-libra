package id.my.ariefwara.cloud.libra.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookDTO(
    Long bookId,

    @NotBlank(message = "ISBN is required")
    String isbn,

    @NotBlank(message = "Title is required")
    String title,

    @NotBlank(message = "Author is required")
    String author
) {}
