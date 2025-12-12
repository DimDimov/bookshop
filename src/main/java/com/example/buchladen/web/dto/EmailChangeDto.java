package com.example.buchladen.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailChangeDto {

    @NotEmpty(message = "{emailDto.newEmail.empty}")
    @Email(message = "{emailDto.newEmail.invalid}")
    private String newEmail;

    @NotEmpty(message = "{emailDto.confirmEmail.empty}")
    @Email(message = "{emailDto.confirmEmail.invalid}")
    private String confirmEmail;

    @Size(min = 6, message = "{emailDto.password.short}")
    private String password;

    public EmailChangeDto(){}

    public EmailChangeDto(String newEmail, String confirmEmail, String password) {
        this.newEmail = newEmail;
        this.confirmEmail = confirmEmail;
        this.password = password;
    }

    public boolean isMatchingEmails() {
        return newEmail != null && newEmail.equals(confirmEmail);
    }
}
