package com.example.buchladen.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Setter
@Getter
public class UserRegistrationDto {

    @NotEmpty(message = "!Bitte geben Sie Ihren Vorname an.")
    private String firstName;
    @NotEmpty (message = "!Bitte geben Sie Ihren Nachname an.")
    private String lastName;
    @DateTimeFormat(pattern="dd-mm-yyyy")
    private String birthday;
    @NotEmpty(message = "!Bitte geben Sie Ihre Straße an.")
    private String street;
    @NotEmpty(message = "!Bitte geben Sie Ihre Hausnummer an.")
    private String houseNumber;
   @NotEmpty(message = "!Bitte geben Sie Ihre Postleitzahl an.")
    private String postcode;
    @NotEmpty(message = "!Bitte geben Sie Ihre Stadt an.")
    private String town;
    private String country;
    @NotEmpty(message = "!Bitte geben Sie eine gültige E-Mail Adresse an.")
    @Email(message = "!Bitte geben Sie eine gültige E-Mail Adresse an.")
    private String email;
    @Size(min = 6, message = "!Ihr Passwort muss mindestens sechs Zeichen lang sein.")
    private String password;
    private String customUsername;
    private Boolean useEmailAsUsername = false;

    private String resetPasswordToken;


    public UserRegistrationDto() {
    }

    public UserRegistrationDto(String email, String password,  String firstName, String lastName,
                               String birthday, String street, String houseNumber, String postcode, String town, String country, String resetPasswordToken,
                               Boolean useEmailAsUsername, String customUsername) {
        super();
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.street = street;
        this.houseNumber = houseNumber;
        this.postcode = postcode;
        this.town = town;
        this.country = country;
      this.resetPasswordToken = resetPasswordToken;
        this.useEmailAsUsername = useEmailAsUsername;
        this.customUsername = customUsername;
    }
}
