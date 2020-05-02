package com.bookshop.bookshop.service.implementation;

import com.bookshop.bookshop.exception.AppException;
import com.bookshop.bookshop.model.Role;
import com.bookshop.bookshop.model.RoleName;
import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.ApiResponse;
import com.bookshop.bookshop.payload.JwtAuthenticationResponse;
import com.bookshop.bookshop.payload.LoginRequest;
import com.bookshop.bookshop.payload.SignUpRequest;
import com.bookshop.bookshop.repository.RoleRepository;
import com.bookshop.bookshop.repository.UserRepository;
import com.bookshop.bookshop.security.JwtTokenProvider;
import com.bookshop.bookshop.service.AuthenticationService;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                                     PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    JwtTokenProvider jwtTokenProvider;
    AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {

        System.out.println("Auth1");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        System.out.println("Auth1");

        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("Auth1");

        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));

    }

    @Override
    public ResponseEntity<?> registerUser(SignUpRequest signUpRequest) {

        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }



        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));



        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);


        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();


        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));

    }
}
