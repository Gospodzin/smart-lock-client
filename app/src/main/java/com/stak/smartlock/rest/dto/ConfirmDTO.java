package com.stak.smartlock.rest.dto;

/**
 * Created by gospo on 03.01.15.
 */
public class ConfirmDTO {
    private String username;
    private String pin;

    public ConfirmDTO() { }

    public ConfirmDTO(String username, String pin) {
        this.username = username;
        this.pin = pin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
