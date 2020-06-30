package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.model.Love;
import com.bookshop.bookshop.model.Story;
import com.bookshop.bookshop.model.Topic;
import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.*;
import com.bookshop.bookshop.repository.LoveRepository;
import com.bookshop.bookshop.repository.StoryRepository;
import com.bookshop.bookshop.repository.TopicRepository;
import com.bookshop.bookshop.repository.UserRepository;
import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.service.StoryService;
import com.bookshop.bookshop.service.UserService;
import com.bookshop.bookshop.service.implementation.StoryServiceImpl;
import com.bookshop.bookshop.service.implementation.UserServiceImpl;
import com.bookshop.bookshop.util.MockUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.support.ArgumentConvertingMethodInvoker;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import javax.swing.text.html.Option;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

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

    @Test
    public void should_return_checkIsUserLikedStory() throws Exception {
        Instant createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        Topic topic  = new Topic();
        topic.setDescription("Topic Description");
        topic.setTitle("Topic Title");
        topic.setCreatedAt(createdAt);
        topic.setUpdatedAt(createdAt);
        topic.setId((long) 123);
        topic.setCreatedBy((long) 12);
        topic.setCreatedAt(createdAt);
        topic.setUpdatedAt(createdAt);

        User user = new User();
        user.setId((long) 12);
        user.setPassword("password");
        user.setEmail("email@dsadasda.dsad");
        user.setName("nanemadsad");
        user.setUsername("sdad");
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(createdAt);
        UserPrincipal userPrincipal = UserPrincipal.create(user);


        Story story = new Story();
        story.setDescription("Story Description");
        story.setTitle("Story Title");
        story.setTopic(topic);
        story.setCreatedBy((long) 123);
        story.setId((long) 333);
        story.setTopic(topic);
        story.setCreatedBy(user.getId());
        story.setUpdatedAt(createdAt);
        story.setCreatedAt(createdAt);

        Mockito.when(loveRepository.existsByStoryIdAndUserId(ArgumentMatchers.any(Long.class), ArgumentMatchers.anyLong())).thenReturn(true);
        Assert.assertEquals(false, userService.checkIsUserLovedStory(user.getId(), story.getId()).getAvailable());
        Assert.assertEquals(true, !userService.checkIsUserLovedStory(user.getId(), story.getId()).getAvailable());

    }

    @Test
    public void should_return_getUserStoriesStatistics() throws Exception {
        Instant createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        Topic topic  = new Topic();
        topic.setDescription("Topic Description");
        topic.setTitle("Topic Title");
        topic.setCreatedAt(createdAt);
        topic.setUpdatedAt(createdAt);
        topic.setId((long) 123);
        topic.setCreatedBy((long) 12);
        topic.setCreatedAt(createdAt);
        topic.setUpdatedAt(createdAt);

        User user = new User();
        user.setId((long) 12);
        user.setPassword("password");
        user.setEmail("email@dsadasda.dsad");
        user.setName("nanemadsad");
        user.setUsername("sdad");
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(createdAt);
        UserPrincipal userPrincipal = UserPrincipal.create(user);

        User user2 = new User();
        user.setId(34344L);
        user.setPassword("password");
        user.setEmail("passsword@example.com");
        user.setName("Joe Doe");
        user.setUpdatedAt(createdAt);
        user.setCreatedAt(createdAt);
        UserPrincipal userPrincipal1 = UserPrincipal.create(user2);

        Story story = new Story();
        story.setDescription("Story Description");
        story.setTitle("Story Title");
        story.setTopic(topic);
        story.setCreatedBy((long) 123);
        story.setId((long) 333);
        story.setTopic(topic);
        story.setCreatedBy(user.getId());
        story.setUpdatedAt(createdAt);
        story.setCreatedAt(createdAt);

        Story story2 = new Story();
        story2.setDescription("Story Description");
        story2.setTitle("Story Title");
        story2.setTopic(topic);
        story2.setCreatedBy((long) 123);
        story2.setId((long) 333);
        story2.setTopic(topic);
        story2.setCreatedBy(user.getId());
        story2.setUpdatedAt(createdAt);
        story2.setCreatedAt(createdAt);

        Love love = new Love(story, user);
        Love love1 = new Love(story, user2);

        List<Love> loves = new ArrayList<>();
        loves.add(love);
        loves.add(love1);

        List<Story> stories = new ArrayList<>();
        stories.add(story);
        stories.add(story2);

        List<Long> storyIds = new ArrayList<>();
        storyIds.add(story.getId());
        storyIds.add(story2.getId());


        UserStoryCount userStoryCount = new UserStoryCount(1, 1, 2);

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");

        PageImpl<Story> storyPage = MockUtil.createMockPage(stories);
        PageImpl<Long> storyIdsPage = MockUtil.createMockPage(storyIds);

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(loveRepository.findLoveStoryIdsByUserId(ArgumentMatchers.any(Long.class), ArgumentMatchers.isA(Pageable.class))).thenReturn(storyIdsPage);
        Mockito.when(storyRepository.countByCreatedBy(ArgumentMatchers.any(Long.class))).thenReturn(1l);
        Mockito.when(loveRepository.countByUserId(ArgumentMatchers.any(Long.class))).thenReturn(1l);
        Mockito.when(storyRepository.findByCreatedBy(ArgumentMatchers.anyLong(), ArgumentMatchers.isA(Pageable.class))).thenReturn(storyPage);
        Mockito.when(loveRepository.findByStoryIdIn(storyIds)).thenReturn(loves);

        Assert.assertEquals(userStoryCount.getStoriesCreated(), userService.getUserStoriesStatistics(user.getId(), userPrincipal, 0, 10).getStoriesCreated());
        Assert.assertEquals(userStoryCount.getStoriesLiked(), userService.getUserStoriesStatistics(user.getId(), userPrincipal, 0, 10).getStoriesLiked());
        Assert.assertEquals(userStoryCount.getLovesOnCreatedStories(), userService.getUserStoriesStatistics(user.getId(), userPrincipal, 0, 10).getLovesOnCreatedStories());



    }




}
