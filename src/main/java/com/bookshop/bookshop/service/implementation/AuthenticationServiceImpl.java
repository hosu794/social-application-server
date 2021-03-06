package com.bookshop.bookshop.service.implementation;

import com.bookshop.bookshop.exception.AppException;
import com.bookshop.bookshop.exception.ResourceNotFoundException;
import com.bookshop.bookshop.model.Role;
import com.bookshop.bookshop.model.RoleName;
import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.*;
import com.bookshop.bookshop.repository.RoleRepository;
import com.bookshop.bookshop.repository.UserRepository;
import com.bookshop.bookshop.security.JwtTokenProvider;
import com.bookshop.bookshop.security.UserPrincipal;
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

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Collections;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {


    public AuthenticationServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                                     PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {



        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);



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

    public ResponseEntity<?> updateName(UserPrincipal currentUser, UpdateNameRequest updateNameRequest) {
       User user = userRepository.findById(currentUser.getId())
               .orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUser.getId()));

       if(userRepository.existsByName(updateNameRequest.getName())) {
           return new ResponseEntity(new ApiResponse(false, "Name already in user"), HttpStatus.BAD_REQUEST);
       }

       user.setName(updateNameRequest.getName());

       User result =  userRepository.save(user);

       return ResponseEntity.ok().body(new ApiResponse(true, "User name updated successfully"));

    }


    public ResponseEntity<?> updatePassword(UserPrincipal currentUser, UpdatePasswordRequest updatePasswordRequest) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(( ) -> new ResourceNotFoundException("User", "id", currentUser.getId()));

        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getPassword()));

        User result = userRepository.save(user);

        return ResponseEntity.ok().body(new ApiResponse(true, "User password updated successfully"));
    }

    public ResponseEntity<?> updateUsername(UserPrincipal currentUser, UpdateUsernameRequest updateUsernameRequest) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUser.getId()));

        if(userRepository.existsByUsername(updateUsernameRequest.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username already in user"), HttpStatus.BAD_REQUEST);
        }

        user.setUsername(updateUsernameRequest.getUsername());

        User result = userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse(true, "User username updated successfully"));
    }
}
