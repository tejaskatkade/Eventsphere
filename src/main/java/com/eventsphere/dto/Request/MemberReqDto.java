package com.eventsphere.dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
public class MemberReqDto {

    private String firstName;

    private String lastName;

    @Email(message = "Please enter the valid email")
    private String email;

    @NotEmpty
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[#@$*]).{5,20}$",
            message = "Password must be 5-20 characters long, contain at least one digit, one lowercase letter, one uppercase letter, and one special character (#@$*).")
    @Length(message = "Invalid password length")
    private String password;

    private String phoneNumber;
}
