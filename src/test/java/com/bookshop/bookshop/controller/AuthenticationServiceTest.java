package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.exception.BadRequestException;
import com.bookshop.bookshop.model.Role;
import com.bookshop.bookshop.model.RoleName;
import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.*;
import com.bookshop.bookshop.repository.RoleRepository;
import com.bookshop.bookshop.repository.UserRepository;
import com.bookshop.bookshop.security.JwtTokenProvider;
import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.service.AuthenticationService;
import com.bookshop.bookshop.service.implementation.AuthenticationServiceImpl;
import com.sun.org.apache.xpath.internal.Arg;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.assertj.core.api.AbstractAssert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

    PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
    AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
    JwtTokenProvider jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);

    AuthenticationService authenticationService = new AuthenticationServiceImpl(userRepository, roleRepository,
            passwordEncoder, authenticationManager ,jwtTokenProvider);

    Instant createdAt;
    User user;
    UserPrincipal userPrincipal;
    Role role;
    MockHttpServletRequest request;
    SignUpRequest signUpRequest;
    LoginRequest loginRequest;
    String secret;
    int mockExpirationTime;
    Authentication authentication;
    UpdateNameRequest updateNameRequest;
    UpdatePasswordRequest updatePasswordRequest;
    UpdateUsernameRequest updateUsernameRequest;

    @Before
    public void initialize() throws Exception {
         createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
         user = new User();
        user.setPassword("Password");
        user.setId((long) 12);
        user.setUsername("Username");
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(createdAt);
        user.setEmail("grzechu@gmail.com");

         userPrincipal = UserPrincipal.create(user);

         role = new Role(RoleName.ROLE_USER);

         request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
         signUpRequest = new SignUpRequest();
        signUpRequest.setName("Name");
        signUpRequest.setUsername("Username");
        signUpRequest.setPassword("Password");
        signUpRequest.setEmail("email@gmail.com");

         loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail(user.getUsername());
        loginRequest.setPassword(user.getPassword());

         secret = "tHIS_mEGO_SECRET_DONT_REGRET";
         mockExpirationTime = 604800000;

         authentication = Mockito.mock(Authentication.class);


         updateNameRequest = new UpdateNameRequest("New Name");
         updatePasswordRequest = new UpdatePasswordRequest("newpassword");

         updateUsernameRequest = new UpdateUsernameRequest("newusername");
    }



    @Test
    public void should_return_register_success() throws Exception {

        Mockito.when(passwordEncoder.encode(ArgumentMatchers.any(String.class))).thenReturn("Encode Password");
        Mockito.when(userRepository.existsByEmail(ArgumentMatchers.any(String.class))).thenReturn(false);
        Mockito.when(userRepository.existsByUsername(ArgumentMatchers.any(String.class))).thenReturn(false);
        Mockito.when(roleRepository.findByName(ArgumentMatchers.any(RoleName.class))).thenReturn(Optional.of(role));
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);


        Assert.assertNotNull(authenticationService.registerUser(signUpRequest));
        Assert.assertEquals(HttpStatus.CREATED, authenticationService.registerUser(signUpRequest).getStatusCode());

    }

    @Test
    public void should_generate_token() throws Exception {

        Mockito.when(authenticationManager.authenticate(ArgumentMatchers.any())).thenReturn(authentication);

        Mockito.when(jwtTokenProvider.generateToken(ArgumentMatchers.any(Authentication.class))).thenReturn(generateMockToken(secret, mockExpirationTime, userPrincipal));
        Assert.assertEquals(HttpStatus.OK, authenticationService.authenticateUser(loginRequest).getStatusCode());
        Assert.assertNotNull(authenticationService.authenticateUser(loginRequest).getBody());

    }

    @Test
    public void should_return_updateName() throws Exception {



        Mockito.when(userRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(user));
        Mockito.when(userRepository.existsByName(ArgumentMatchers.anyString())).thenReturn(false);

        user.setName(updateNameRequest.getName());


      Assert.assertEquals(200, authenticationService.updateName(userPrincipal, updateNameRequest).getStatusCodeValue());
        Assert.assertNotNull(authenticationService.updateName(userPrincipal, updateNameRequest).getBody());
    }

    @Test
    public void should_return_updatePassword() throws Exception {




        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));

        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getPassword()));

        Assert.assertEquals(200, authenticationService.updatePassword(userPrincipal, updatePasswordRequest).getStatusCodeValue());
        Assert.assertNotNull(authenticationService.updatePassword(userPrincipal, updatePasswordRequest).getBody());


    }

    @Test
    public void should_return_updateUsername() throws Exception {




        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.existsByUsername(ArgumentMatchers.anyString())).thenReturn(false);

        user.setUsername(updateUsernameRequest.getUsername());



        Assert.assertEquals(200, authenticationService.updateUsername(userPrincipal, updateUsernameRequest).getStatusCodeValue());
        Assert.assertNotNull(authenticationService.updateUsername(userPrincipal, updateUsernameRequest).getBody());


    }

 
    private String generateMockToken(String secret, int expirationTime, UserPrincipal userPrincipal) {

        Date now = new Date();
        Date expriryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expriryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }



}
