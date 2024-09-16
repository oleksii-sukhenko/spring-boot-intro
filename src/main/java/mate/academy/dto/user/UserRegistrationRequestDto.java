package mate.academy.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import mate.academy.validate.FieldMatch;

@Getter
@Setter
@FieldMatch(
        first = "password",
        second = "repeatPassword",
        message = "The password fields must match."
)
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 8, max = 16)
    private String password;
    @NotBlank
    @Size(min = 8, max = 16)
    private String repeatPassword;
    @NotBlank
    @Size(min = 1)
    private String firstName;
    @NotBlank
    @Size(min = 1)
    private String lastName;
    private String shippingAddress;
}
