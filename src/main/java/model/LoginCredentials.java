package model;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginCredentials {
    private String login;
    private String password;

    public LoginCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static LoginCredentials fromCourier(Courier courier) {
        return new LoginCredentials(courier.getLogin(), courier.getPassword());
    }
}
