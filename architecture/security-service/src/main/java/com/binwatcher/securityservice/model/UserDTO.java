package com.binwatcher.securityservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String email;
    private String name;
    private List<String> roles;
}
