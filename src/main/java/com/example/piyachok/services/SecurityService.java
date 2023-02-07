package com.example.piyachok.services;

import com.example.piyachok.constants.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;


public class SecurityService {
    public static boolean authorizedUserHasRole(String role) {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return authorities
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().split("_")[1].equals(role));
    }

    public static String getLoginAuthorizedUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
