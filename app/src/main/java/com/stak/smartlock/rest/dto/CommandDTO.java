package com.stak.smartlock.rest.dto;

/**
 * Created by gospo on 03.01.15.
 */
public class CommandDTO {
    private String token;
    private String command;

    public CommandDTO() { }

    public CommandDTO(String token, String command) {
        this.token = token;
        this.command = command;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
