package com.vishwaraj.userService.service;

import com.vishwaraj.userService.domain.AuthenticationRequest;
import com.vishwaraj.userService.domain.AuthenticationResponse;
import com.vishwaraj.userService.domain.Users;
import com.vishwaraj.userService.exception.CustomErrorMessage;
import com.vishwaraj.userService.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    public ResponseEntity<?> createJWTToken(AuthenticationRequest authenticationRequest) throws Exception{
        log.info("Inside the createJWTToken for {}", authenticationRequest);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getUserPassword()
                    ));
        }catch (BadCredentialsException e){
            CustomErrorMessage errorMessage = new CustomErrorMessage();
            errorMessage.setStatus("401");
            errorMessage.setMessage("Incorrect Username and Password!!!");
            return ResponseEntity.badRequest().body(errorMessage);
        }
        final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUserName());
        log.info("User Details has been loaded successfully {}", userDetails);


        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        return  ResponseEntity.ok().body(jwtUtil.generateToken(userDetails));
    }
}
