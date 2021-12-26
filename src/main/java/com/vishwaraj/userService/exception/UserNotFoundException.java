package com.vishwaraj.userService.exception;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class UserNotFoundException extends RuntimeException {
    private String message;
    private String details;
}