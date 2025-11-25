package com.example.buchladen.web.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDto {

    @NotEmpty(message= "!Bitte geben Sie neues Passwort ein.")
    @Size(message = "!Ihr Passwort muss mindestens sechs Zeichen lang sein.")
    private String newPassword;

    @NotEmpty(message= "!Bitte wiederholen Sie neues Passwort.")
    @Size(message = "!Ihr Passwort muss mindestens sechs Zeichen lang sein.")
    private String confirmNewPassword;

    private String password;

    public ChangePasswordDto() {};

    public ChangePasswordDto(String newPassword, String confirmNewPassword) {
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
        this.password = password;
    }

    public boolean isMatchingPassword() {
        return newPassword != null && newPassword.equals(confirmNewPassword);
    }
}
