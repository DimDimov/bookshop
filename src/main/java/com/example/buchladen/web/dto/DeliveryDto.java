package com.example.buchladen.web.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeliveryDto {

    private String firstName;
    private String lastName;
    private String street;
    private String town;
    private String country;
    private String postcode;
    private String houseNumber;
    private boolean isNew;
}
