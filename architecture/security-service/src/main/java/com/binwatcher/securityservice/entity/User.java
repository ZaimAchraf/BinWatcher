package com.binwatcher.securityservice.entity;

import com.binwatcher.securityservice.model.Authority;
import com.binwatcher.securityservice.model.Role;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    String id;
    @Setter
    String email;
    @Getter @Setter
    String name;
    @Setter
    String password;
    @Setter
    List<Authority> roles;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
