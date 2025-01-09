package com.binwatcher.securityservice.model;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public class Authority implements GrantedAuthority {
    Role role;

    @Override
    public String getAuthority() {
        return role.toString();
    }
}
