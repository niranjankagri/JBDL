package jbdl.session08.books;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * What a client sends for POST and PUT (a DTO). The id is not in it: the server assigns ids. The
 * annotations are checked by @Valid in the controller; a violation becomes a 400 response.
 */
public record BookRequest(
        @NotBlank String title,
        @NotBlank String author,
        @NotBlank @Pattern(regexp = "\\d{13}", message = "must be 13 digits") String isbn,
        @NotNull @Positive @Digits(integer = 6, fraction = 2) BigDecimal price,
        @Min(1450) @Max(2100) int publishedYear) {
}
