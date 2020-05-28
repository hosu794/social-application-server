package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.EmailRequest;
import com.bookshop.bookshop.payload.UserProfile;
import com.bookshop.bookshop.payload.UserSummary;
import com.bookshop.bookshop.payload.UsernameRequest;
import com.bookshop.bookshop.repository.LoveRepository;
import com.bookshop.bookshop.repository.StoryRepository;
import com.bookshop.bookshop.repository.TopicRepository;
import com.bookshop.bookshop.repository.UserRepository;
import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.service.StoryService;
import com.bookshop.bookshop.service.UserService;
import com.bookshop.bookshop.service.implementation.StoryServiceImpl;
import com.bookshop.bookshop.service.implementation.UserServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    UserRepository userRepository = Mockito.mock(UserRepository.class);
    StoryRepository storyRepository = Mockito.mock(StoryRepository.class);
    LoveRepository loveRepository = Mockito.mock(LoveRepository.class);
    StoryService storyService = Mockito.mock(StoryService.class);
    UserServiceImpl userService = new UserServiceImpl(userRepository, storyRepository, loveRepository, storyService);

    @Test
    public void should_return_current_user() throws Exception {
        Instant createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        User user = new User();
        user.setPassword("Password");
        user.setId((long) 12);
        user.setUsername("Username");
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(createdAt);
        user.setEmail("grzechu@gmail.com");

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        UserSummary userSummary = new UserSummary(user.getId(), user.getUsername(), user.getName());

        Assert.assertEquals(userSummary.getName(), userService.getCurrentUser(userPrincipal).getName());

    }

    @Test
    public void should_return_checkAvailabilityUsername() throws Exception {
        Instant createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        User user = new User();
        user.setPassword("Password");
        user.setId((long) 12);
        user.setUsername("Username");
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(createdAt);
        user.setEmail("grzechu@gmail.com");

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        UsernameRequest usernameRequest = new UsernameRequest();
        usernameRequest.setUsername(user.getName());
        Assert.assertEquals(true, userService.checkUsernameAvailability(usernameRequest).getAvailable());

    }


    @Test
    public void should_return_checkAvailabilityEmail() throws Exception {
        Instant createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        User user = new User();
        user.setPassword("Password");
        user.setId((long) 12);
        user.setUsername("Username");
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(createdAt);
        user.setEmail("grzechu@gmail.com");

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        Mockito.when(userRepository.existsByEmail(ArgumentMatchers.anyString())).thenReturn(false);

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmail(user.getEmail());

        Assert.assertEquals(true, userService.checkEmailAvailability(emailRequest).getAvailable());
    }

    @Test
    public void should_return_getUserProfile() throws Exception {
        Instant createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        User user = new User();
        user.setPassword("Password");
        user.setId((long) 12);
        user.setUsername("Username");
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(createdAt);
        user.setEmail("grzechu@gmail.com");

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        long storyCount = 5;
        long loveCount = 1021;

        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt(), storyCount, loveCount);

        Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(Optional.of(user));
        Mockito.when(storyRepository.countByCreatedBy(ArgumentMatchers.anyLong())).thenReturn(storyCount);
        Mockito.when(loveRepository.countByUserId(ArgumentMatchers.anyLong())).thenReturn(loveCount);

        Assert.assertEquals(userProfile.getUsername(), userService.getUserProfile(user.getUsername()).getUsername());
        Assert.assertEquals(userProfile.getLoveCount(), userService.getUserProfile(user.getUsername()).getLoveCount());
        Assert.assertEquals(userProfile.getName(), userService.getUserProfile(user.getUsername()).getName());

    }


}