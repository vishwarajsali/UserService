package com.vishwaraj.userService.service;

import com.vishwaraj.userService.domain.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    Users saveUser(Users users);
    Optional<?> getAll();
}
