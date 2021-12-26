package com.vishwaraj.userService.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.ArrayList;
import java.util.Collection;

@Document("users")
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String userName;
    private String userId;
    private String userPassword;
    private Collection<Roles> roles = new ArrayList<>();
}
