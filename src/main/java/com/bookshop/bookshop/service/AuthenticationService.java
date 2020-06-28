package com.bookshop.bookshop.service;

import com.bookshop.bookshop.payload.LoginRequest;
import com.bookshop.bookshop.payload.SignUpRequest;
import com.bookshop.bookshop.payload.UpdateNameRequest;
import com.bookshop.bookshop.payload.UpdatePasswordRequest;
import com.bookshop.bookshop.security.UserPrincipal;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest);

    public ResponseEntity<?> registerUser(SignUpRequest signUpRequest);

    public ResponseEntity<?> updateName(UserPrincipal currentUser, UpdateNameRequest updateNameRequest);

    public ResponseEntity<?> updatePassword(UserPrincipal currentUser, UpdatePasswordRequest updatePasswordRequest);
}
