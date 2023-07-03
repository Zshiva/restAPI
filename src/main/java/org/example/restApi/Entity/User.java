package org.example.restApi.Entity;

import java.util.List;

public class User {
    private int id;
    private String Username;
    private String Password;
    private String Role;
    private String Permissions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        this.Role = role;
    }

    public String getPermissions() {
        return Permissions;
    }

    public void setPermissions(String permissions) {
        this.Permissions = permissions;
    }
}
