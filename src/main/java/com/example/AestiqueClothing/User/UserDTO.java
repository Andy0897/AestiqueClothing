package com.example.AestiqueClothing.User;

import jakarta.validation.constraints.*;

public class UserDTO {
    @NotEmpty(message = "Полето е задължително")
    @Size(min = 2, message = "Името трябва да съдържа поне 2 символа")
    @Size(max = 20, message = "Името не трябва да надвишава 20 символа")
    private String firstName;

    @NotEmpty(message = "Полето е задължително")
    @Size(min = 2, message = "Фамилията трябва да съдържа поне 2 символа")
    @Size(max = 20, message = "Фамилията не трябва да надвишава 20 символа")
    private String lastName;

    @NotEmpty(message = "Полето е задължително")
    @Size(min = 6, message = "Потребителското име трябва да съдържа поне 6 символа")
    @Size(max = 14, message = "Потребителското име не трябва да надвишава 14 символа")
    private String username;

    @NotEmpty(message = "Полето е задължително")
    @Size(min = 5, message = "Имейлът трябва да съдържа поне 5 символа")
    private String email;

    @NotEmpty(message = "Полето е задължително")
    @Size(min = 8, message = "Паролата трябва да съдържа поне 8 символа")
    @Size(max = 14, message = "Паролата не трябва да надвишава 14 символа")
    private String password;

    @AssertTrue(message = "Трябва да се съгласите с условията за ползване")
    private Boolean agreeToTerms;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAgreeToTerms() {
        return agreeToTerms;
    }

    public void setAgreeToTerms(Boolean agreeToTerms) {
        this.agreeToTerms = agreeToTerms;
    }
}
