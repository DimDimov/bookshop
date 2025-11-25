package com.example.buchladen.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShippingRegistrationDto {

    private Long id;
    @NotBlank(message = "!Bitte geben Sie Ihren Vorname an.")
    private String firstName;
    @NotEmpty(message = "!Bitte geben Sie Ihren Nachname an.")
    private String lastName;
    @NotBlank(message = "!Bitte geben Sie Ihre Straße an.")
    private String street;
    @NotEmpty(message = "!Bitte geben Sie Ihre Hausnummer an.")
    private String houseNumber;
    @NotEmpty(message = "!Bitte geben Sie Ihre Postleitzahl an.")
    private String postcode;
    @NotEmpty(message = "!Bitte geben Sie Ihre Stadt an.")
    private String town;
    private String country;

    public ShippingRegistrationDto(){
    }

    public ShippingRegistrationDto(String firstName, String lastName, String street,
                                   String houseNumber, String postcode, String town, String country) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.houseNumber = houseNumber;
        this.postcode = postcode;
        this.town = town;
        this.country = country;
    }


}
