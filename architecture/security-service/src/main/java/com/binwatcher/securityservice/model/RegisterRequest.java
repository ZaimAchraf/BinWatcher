package com.binwatcher.securityservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RegisterRequest {
    private String login;
    private String name;
    private String password;
    private List<Role> roles;
}
