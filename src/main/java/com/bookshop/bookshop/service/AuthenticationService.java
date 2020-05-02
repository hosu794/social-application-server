package com.bookshop.bookshop.service;

import com.bookshop.bookshop.payload.LoginRequest;
import com.bookshop.bookshop.payload.SignUpRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest);

    public ResponseEntity<?> registerUser(SignUpRequest signUpRequest);

}
