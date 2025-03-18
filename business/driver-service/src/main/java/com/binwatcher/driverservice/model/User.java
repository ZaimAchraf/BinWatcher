package com.binwatcher.driverservice.model;

import lombok.*;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User{

    String id;
    String email;
    String name;
    String password;
    List<String> roles;

}
