package com.example.buchladen.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailChangeDto {

    @NotEmpty(message = "!Bitte geben Sie eine E-Mail Adresse an.")
    @Email(message = "!Bitte geben Sie eine gültige E-Mail Adresse an.")
    private String newEmail;

    @NotEmpty(message = "!Bitte geben Sie eine E-Mail Adresse an.")
    @Email(message = "!Bitte geben Sie eine gültige E-Mail Adresse an.")
    private String confirmEmail;

    @Size(min = 6, message = "!Ihr Passwort muss mindestens sechs Zeichen lang sein.")
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
