package com.binwatcher.driverservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserInfos {
    private String login;
    private String name;
    private String password;
    private List<String> roles;
}
