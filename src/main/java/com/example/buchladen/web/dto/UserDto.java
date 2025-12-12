package com.example.buchladen.web.dto;


import com.example.buchladen.Model.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class UserDto {

    private Long id;
  private String customUsername;
  private String email;
  @NotEmpty(message = "{userDto.firstName.notBlank}")
  private String firstName;
   @NotEmpty (message = "{userDto.lastName.notBlank}")
  private String lastName;
    @NotEmpty(message = "{userDto.town.notBlank}")
  private String town;
    @NotEmpty(message = "{userDto.street.notBlank}")
  private String street;
    private String country;
    @NotEmpty(message = "{userDto.houseNumber.notBlank}")
    private String houseNumber;
    @NotEmpty(message = "{userDto.postcode.notBlank}")
    private String postcode;
    private Boolean useEmailAsUsername;
    private boolean enabled;
    private String birthday;
    private String password;

    public UserDto(Long id, String customUsername, String email, String firstName, String lastName, boolean useEmailAsUsername,
                   String town, String street, String country, String houseNumber, String postcode, boolean enabled, String birthday, String password) {
        this.id = id;
        this.customUsername = customUsername;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.town = town;
        this.street = street;
        this.country = country;
        this.houseNumber = houseNumber;
        this.postcode = postcode;
        this.useEmailAsUsername = useEmailAsUsername;
        this.enabled = enabled;
        this.birthday = birthday;
        this.password = password;
    }

}
