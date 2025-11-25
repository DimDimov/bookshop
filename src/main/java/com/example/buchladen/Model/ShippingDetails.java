package com.example.buchladen.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="delivery")
@NoArgsConstructor
public class ShippingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "street")
    private String street;

    @Column(name = "houseNumber")
    private String houseNumber;

    @Column(name = "postcode")
    private String postcode;

    @Column (name = "town")
    private String town;

    @Column(name = "country")
    private String country;

    @Column(name= "isDefault")
    private boolean isDefault;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    public ShippingDetails(String firstName, String lastName, String street, String houseNumber, String postcode,
                           String town, String country){
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.houseNumber = houseNumber;
        this.postcode = postcode;
        this.town = town;
        this.country = country;
    }


}
