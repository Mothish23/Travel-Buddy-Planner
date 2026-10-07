package com.travelbuddy.service;

import com.travelbuddy.dto.*;
import com.travelbuddy.entity.User;
import com.travelbuddy.repository.UserRepository;
import com.travelbuddy.security.JwtService;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public AuthService(UserService userService, UserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequest request) {
        userService.register(request.getName(), request.getEmail(), request.getPassword());
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().toLowerCase().trim();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        User user = userRepository.findByEmail(email).orElseThrow();
        return new AuthResponse(token, user.getName(), user.getEmail(), user.getRole().name());
    }
}