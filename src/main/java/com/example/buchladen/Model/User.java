package com.example.buchladen.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
/*@AllArgsConstructor*/
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(name = "firstName")
    private String firstName;

    @Column (name="lastName")
    private String lastName;

    @Column(name= "birthday")
    private String birthday;

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

    @Column (name="email", unique = true)
    private String email;

    @Column (name="password")
    private String password;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column(name = "customUsername", unique = true)
    private String customUsername;

    @Column(name = "useEmailAsUsername", nullable = false)
    private Boolean useEmailAsUsername;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;


    public User(String email, String password, String firstName, String lastName, String birthday,
                String street, String houseNumber, String postcode, String town, String country, String resetPasswordToken,
                String customUsername, Boolean useEmailAsUsername, Boolean enabled) {
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
        this.customUsername = customUsername;
        this.useEmailAsUsername = useEmailAsUsername;
        this.enabled = enabled;

    }

@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Order> orders = new ArrayList<>();


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ShippingDetails> shippingDetailsList = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_preferences",
            joinColumns = @JoinColumn(name= "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> preferredBooks = new ArrayList<>();
}
