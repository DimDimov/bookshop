package com.example.buchladen.web.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangeUserNameDto {

    @NotEmpty(message = "!Bitte geben Sie neuer Benutzername ein.")
    private String customUsername;

    @NotEmpty(message = "!Bitte wiederholen Sie neuer Benutzername.")
    private String confirmCustomUsername;

    @Size(min = 6, message = "!Ihr Passwort muss mindestens sechs Zeichen lang sein.")
    private String password;

    private Boolean useEmailAsUsername = false;

    private String email;

    public ChangeUserNameDto() {}

    public ChangeUserNameDto(String customUsername, String confirmCustomUsername, String password, Boolean useEmailAsUsername, String email) {
        this.customUsername = customUsername;
        this.confirmCustomUsername = confirmCustomUsername;
        this.password = password;
        this.useEmailAsUsername = useEmailAsUsername;
        this.email = email;
    }
}
