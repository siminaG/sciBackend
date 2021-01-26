package com.simina.sci.controller;

import com.simina.sci.model.ERoleName;
import com.simina.sci.model.Role;
import com.simina.sci.model.User;
import com.simina.sci.payload.JwtAuthenticationResponse;
import com.simina.sci.payload.LoginRequest;
import com.simina.sci.repository.RoleRepository;
import com.simina.sci.repository.UserRepository;
import com.simina.sci.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")

public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, loginRequest.getUsername()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody LoginRequest loginRequest){
        User user = new User(loginRequest.getUsername(), loginRequest.getPassword());
        Role userRole = roleRepository.findByName(ERoleName.ROLE_USER).orElseThrow(() -> new AppException("User Role not set."));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(userRole));

        userRepository.saveAndFlush(user);

        return ResponseEntity.ok(new ApiResponse(true, "SignUp succeeded"));

    }


}
