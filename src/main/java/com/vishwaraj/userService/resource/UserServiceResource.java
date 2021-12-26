package com.vishwaraj.userService.resource;

import com.vishwaraj.userService.domain.AuthenticationRequest;
import com.vishwaraj.userService.domain.Users;
import com.vishwaraj.userService.service.AuthenticationService;
import com.vishwaraj.userService.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1")
@Slf4j
public class UserServiceResource {
    @Autowired
    private final UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    public UserServiceResource(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/healthcheck")
    public ResponseEntity<?> healthcheck() {
        log.info("Inside the healthcheck");
        Map<String, String> body = new HashMap<>();
        body.put("Status", "200");
        body.put("message", "healthcheck OK");
        return ResponseEntity.ok().body(body);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticateToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
        return authenticationService.createJWTToken(authenticationRequest);
    }

    @PostMapping("/addService")
    public ResponseEntity<?> addUser(@RequestBody Users user){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/v1/addService").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        log.info("Inside the healthcheck");
        return ResponseEntity.ok().body(userService.getAll());
    }

}
