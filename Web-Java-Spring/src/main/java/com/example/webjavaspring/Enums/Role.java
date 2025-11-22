package com.example.webjavaspring.Enums;

public enum Role {
    USER,
    ADMIN;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}