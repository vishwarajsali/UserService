package com.vishwaraj.userService.service;

import com.vishwaraj.userService.domain.Users;
import com.vishwaraj.userService.exception.UserNotFoundException;
import com.vishwaraj.userService.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Users saveUser(Users users) {
        users.setUserPassword(passwordEncoder.encode(users.getUserPassword()));

        return userRepository.save(users);
    }

    @Override
    public Optional<?> getAll() {
        return Optional.of(userRepository.findAll());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUserId(username);
        if(user == null){
            log.error("UserName {} does not exist", username);
            throw new UsernameNotFoundException(String.format("UserName {} does not exist", username));
        }
        log.info("UserName {} exist", username);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(roles -> authorities.add(new SimpleGrantedAuthority(roles.name())));
        return new User(user.getUserId(), user.getUserPassword(), authorities);
    }

}
