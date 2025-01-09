package com.binwatcher.securityservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginRequest {
    private String login;
    private String password;
}
