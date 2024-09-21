package mate.academy.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record UserLoginRequestDto(
        @NotEmpty
        @Email
        String email,
        @NotEmpty
        @Length(min = 8, max = 16)
        String password
) {
}
