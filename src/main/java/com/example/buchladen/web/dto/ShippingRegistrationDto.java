package com.example.buchladen.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShippingRegistrationDto {

    private Long id;
    @NotBlank(message = "{userDto.firstName.notBlank}")
    private String firstName;
    @NotEmpty(message = "{userDto.lastName.notBlank}")
    private String lastName;
    @NotBlank(message = "{userDto.street.notBlank}")
    private String street;
    @NotEmpty(message = "{userDto.houseNumber.notBlank}")
    private String houseNumber;
    @NotEmpty(message = "{userDto.postcode.notBlank}")
    private String postcode;
    @NotEmpty(message = "{userDto.town.notBlank}")
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
