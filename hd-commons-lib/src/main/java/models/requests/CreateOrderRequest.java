package models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrderRequest(
        @Schema(description = "Requester ID", example = "64bb3bbe319d2b6e43ba05dd")
        @NotBlank(message = "The requesterId cannot be null or blank")
        @Size(min = 24, max = 36, message = "The requesterId must be between 24 and 36 characters")
        String requesterId,

        @Schema(description = "Customer ID", example = "64bb3bbe319d2b6e43ba05dd")
        @NotBlank(message = "The customerId cannot be null or blank")
        @Size(min = 24, max = 36, message = "The customerId must be between 24 and 36 characters")
        String customerId,

        @Schema(description = "Title of order", example = "Fix my computer")
        @NotBlank(message = "The title cannot be null or blank")
        @Size(min = 3, max = 45, message = "The title must be between 3 and 45 characters")
        String title,

        @Schema(description = "Description of order", example = "My computer is broken")
        @NotBlank(message = "The description cannot be null or blank")
        @Size(min = 10, max = 3000, message = "The description must be between 10 and 3000 characters")
        String description,

        @Schema(description = "Status of order", example = "Open")
        @NotBlank(message = "The status cannot be null or blank")
        @Size(min = 4, max = 15, message = "The status must be between 4 and 15 characters")
        String status
) { }
