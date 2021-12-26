package com.vishwaraj.userService.domain;

import lombok.Data;

@Data
public class AuthenticationRequest {
    String userName;
    String userPassword;
}
