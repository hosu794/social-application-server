package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.exception.AppException;
import com.bookshop.bookshop.model.Role;
import com.bookshop.bookshop.model.RoleName;
import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.*;
import com.bookshop.bookshop.repository.RoleRepository;
import com.bookshop.bookshop.repository.UserRepository;
import com.bookshop.bookshop.security.CurrentUser;
import com.bookshop.bookshop.security.JwtTokenProvider;
import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.service.implementation.AuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.xml.ws.Response;
import java.net.URI;
import java.util.Collections;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationService.authenticateUser(loginRequest);
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authenticationService.registerUser(signUpRequest);
    }

    @PutMapping("/update/name")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateName(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody UpdateNameRequest updateNameRequest) {
        return authenticationService.updateName(currentUser, updateNameRequest);
    }

    @PutMapping("/update/password")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updatePassword(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        return authenticationService.updatePassword(currentUser, updatePasswordRequest);
    }

}
