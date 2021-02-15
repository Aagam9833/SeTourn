package com.example.setourn.Players;

public class Players {
    String Username,Type;

    public Players() {
    }

    public Players(String username, String type) {
        Username = username;
        Type = type;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
