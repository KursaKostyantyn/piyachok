package com.example.piyachok.constants;

public enum Role {

    ROLE_SUPERADMIN("SUPERADMIN"),
    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER");

     final String userRole;

    public String getUserRole() {
        return userRole;
    }

    Role(String userRole) {
        this.userRole = userRole;
    }
}
