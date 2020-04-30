package com.bookshop.bookshop.security;

import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomUserDetailsServiceTest {

    UserRepository userRepository = Mockito.mock(UserRepository.class);
    CustomUserDetailsService customUserDetailsService = new CustomUserDetailsService(userRepository);

    @Test
    void should_loadUserByUsername() throws Exception {
        Instant createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        User user = new User();
        user.setPassword("Password");
        user.setId((long) 12);
        user.setUsername("Username");
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(createdAt);
        user.setEmail("grzechu@gmail.com");

        UserPrincipal userPrincipal = UserPrincipal.create(user);
        Mockito.when(userRepository.findByUsernameOrEmail(ArgumentMatchers.any(String.class), ArgumentMatchers.any(String.class))).thenReturn(Optional.of(user));

        Assert.assertEquals(userPrincipal, customUserDetailsService.loadUserByUsername(user.getEmail()));
        Assert.assertEquals(userPrincipal.getUsername(), customUserDetailsService.loadUserByUsername(user.getUsername()).getUsername());
        Assert.assertEquals(userPrincipal.getPassword(), customUserDetailsService.loadUserByUsername(user.getUsername()).getPassword());

    }

    @Test
    void should_loadUserById() throws Exception {
        Instant createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        User user = new User();
        user.setPassword("Password");
        user.setId((long) 12);
        user.setUsername("Username");
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(createdAt);
        user.setEmail("grzechu@gmail.com");

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        Mockito.when(userRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(user));

        Assert.assertEquals(userPrincipal, customUserDetailsService.loadUserById(user.getId()));
        Assert.assertEquals(userPrincipal.getPassword(), customUserDetailsService.loadUserById(user.getId()).getPassword());
        Assert.assertEquals(userPrincipal.getUsername(), customUserDetailsService.loadUserById(user.getId()).getUsername());

    }
}