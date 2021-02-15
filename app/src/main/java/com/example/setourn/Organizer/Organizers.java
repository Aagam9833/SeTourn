package com.example.setourn.Organizer;

public class Organizers {
    String Username,Type;

    public Organizers(String username) {
        Username = username;
    }

    public Organizers(String username, String type) {
        Username = username;
        Type = type;
    }

    public Organizers() {
    }

    public String getUsername() {
        return Username;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
