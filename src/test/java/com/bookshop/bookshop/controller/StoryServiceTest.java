package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.exception.BadRequestException;
import com.bookshop.bookshop.model.Love;
import com.bookshop.bookshop.model.Story;
import com.bookshop.bookshop.model.Topic;
import com.bookshop.bookshop.model.User;

import com.bookshop.bookshop.payload.ApiResponse;
import com.bookshop.bookshop.payload.StoryRequest;

import com.bookshop.bookshop.repository.LoveRepository;
import com.bookshop.bookshop.repository.StoryRepository;
import com.bookshop.bookshop.repository.TopicRepository;
import com.bookshop.bookshop.repository.UserRepository;
import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.service.StoryService;
import com.bookshop.bookshop.service.implementation.StoryServiceImpl;

import com.bookshop.bookshop.util.MockUtil;
import net.bytebuddy.asm.Advice;
import org.junit.Assert;
import org.junit.Test;

import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.ArgumentMatcherAction;
import org.mockito.junit.MockitoJUnitRunner;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StoryServiceTest {


    StoryRepository storyRepository = mock(StoryRepository.class);
    UserRepository userRepository = mock(UserRepository.class);
    LoveRepository loveRepository = mock(LoveRepository.class);
    TopicRepository topicRepository = mock(TopicRepository.class);
    StoryService storyService = new StoryServiceImpl(storyRepository, userRepository, loveRepository, topicRepository);



    @Test
    public void should_return_correct_story_by_id() throws Exception {

        Topic topic = new Topic();
        topic.setDescription("Topic Description");
        topic.setTitle("Topic title");
        topic.setId((long) 1);

        Story story = new Story();
        story.setCreatedBy((long) 1);
        story.setId((long) 1);
        story.setTopic(topic);
        story.setCreatedBy((long) 1);
        story.setTitle("Story Title");
        story.setBody("<p>Body</p>");
        story.setDescription("Story Description");

        User user = new User();
        user.setUsername("hosu794");
        user.setPassword("password");
        user.setName("Grzegorz Szczęsny");
        user.setEmail("hosu794@gmail.com");
        UserPrincipal userPrincipal = UserPrincipal.create(user);

        when(userRepository.findById((long) 1)).thenReturn(Optional.of(user));
        when(storyRepository.findById((long) 1)).thenReturn(Optional.of(story));
        Assert.assertTrue(storyService.getStoryById( (long) 1, userPrincipal).getTitle().contains("Story Title"));
        Assert.assertTrue(storyService.getStoryById((long) 1, userPrincipal).getBody().contains("<p>Body</p>"));
        Assert.assertTrue(storyService.getStoryById((long) 1, userPrincipal).getDescription().contains("Story Description"));
        Assert.assertNotNull(storyService.getStoryById((long) 1, userPrincipal).getCreatedBy());
        Assert.assertNotNull(storyService.getStoryById((long) 1, userPrincipal).getId());
        Assert.assertNotNull(storyService.getStoryById((long) 1, userPrincipal).getTotalLoves());

    }

    @Test
    public void should_return_getStoryByUsername() throws Exception {

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
        story.setBody("<p>Simple Body</p>");
        story.setUpdatedBy(user.getId());


        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
        List<Story> storiesList = new ArrayList<>();
        storiesList.add(story);
        List<Long> listOfLoves = new ArrayList<>();
        listOfLoves.add((long) 53412);
        listOfLoves.add((long) 1323232);
        listOfLoves.add((long) 13434);
        int total = storiesList.size();
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min(start + pageable.getPageSize(), total);


        List<Story> output = new ArrayList<>();
        output = storiesList.subList(start, end);
        PageImpl page = new PageImpl<>(output, pageable, total);

        List<User> users = new ArrayList<>();
        users.add(user);

        Mockito.when(storyRepository.findByTitle(ArgumentMatchers.any(String.class), ArgumentMatchers.isA(Pageable.class))).thenReturn(page);
        Mockito.when(loveRepository.countByStoryId(ArgumentMatchers.any(Long.class))).thenReturn(user.getId());
        Mockito.when(userRepository.findByIdIn(ArgumentMatchers.any(List.class))).thenReturn(users);

        Assert.assertTrue(storyService.getStoriesByTitle(story.getTitle() , userPrincipal, 0,10).getContent().get(0).getTitle().contains(story.getTitle()));
        Assert.assertTrue(storyService.getStoriesByTitle(story.getTitle(), userPrincipal, 0,10).getContent().get(0).getDescription().contains(story.getDescription()));
        Assert.assertTrue(storyService.getStoriesByTitle(story.getTitle(), userPrincipal, 0,10).getContent().get(0).getTopic().getDescription().contains(topic.getDescription()));

    }

    @Test
    public void should_return_getAllStories_method() throws Exception {


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


        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
        List<Story> storiesList = new ArrayList<>();
        storiesList.add(story);
        List<Long> listOfLoves = new ArrayList<>();
        listOfLoves.add((long) 53412);
        listOfLoves.add((long) 1323232);
        listOfLoves.add((long) 13434);
        int total = storiesList.size();
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min(start + pageable.getPageSize(), total);


        List<Story> output = new ArrayList<>();
        output = storiesList.subList(start, end);
        PageImpl page = new PageImpl<>(output, pageable, total);

        List<User> users = new ArrayList<>();
        users.add(user);


        Mockito.when(storyRepository.findAll(ArgumentMatchers.isA(Pageable.class))).thenReturn(page);
        Mockito.when(userRepository.findByIdIn(ArgumentMatchers.any(List.class))).thenReturn(users);
        Mockito.when(loveRepository.countByStoryId(ArgumentMatchers.any(Long.class))).thenReturn(user.getId());
        Assert.assertTrue(storyService.getAllStories(userPrincipal, 0,10).getContent().get(0).getTitle().contains(story.getTitle()));
        Assert.assertTrue(storyService.getAllStories(userPrincipal, 0,10).getContent().get(0).getDescription().contains(story.getDescription()));
        Assert.assertTrue(storyService.getAllStories(userPrincipal, 0,10).getContent().get(0).getTopic().getDescription().contains(topic.getDescription()));


    }

    @Test
    public void should_return_getStoriesCreatedBy_method() throws Exception {

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


        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
        List<Story> storiesList = new ArrayList<>();
        storiesList.add(story);
        List<Long> listOfLoves = new ArrayList<>();
        listOfLoves.add((long) 53412);
        listOfLoves.add((long) 1323232);
        listOfLoves.add((long) 13434);
        int total = storiesList.size();
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min(start + pageable.getPageSize(), total);


        List<Story> output = new ArrayList<>();
        output = storiesList.subList(start, end);
        PageImpl page = new PageImpl<>(output, pageable, total);

        List<User> users = new ArrayList<>();
        users.add(user);
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(storyRepository.findByCreatedBy(any(Long.class), isA(Pageable.class))).thenReturn(page);
        Assert.assertTrue(storyService.getStoriesCreatedBy("dasda", userPrincipal, 0, 10).getContent().get(0).getTitle().contains(story.getTitle()));
        Assert.assertTrue(storyService.getStoriesCreatedBy("dasda", userPrincipal, 0, 10).getContent().get(0).getDescription().contains(story.getDescription()));
        Assert.assertTrue(storyService.getStoriesCreatedBy("dasda", userPrincipal, 0, 10).getContent().get(0).getTopic().getTitle().contains(topic.getTitle()));
        Assert.assertNotNull(storyService.getStoriesCreatedBy("dasda", userPrincipal, 0, 10).getContent().get(0).getTopic().getCreatedBy());

    }

    @Test
    public void should_return_getStoriesLovedBy_method() throws Exception {
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
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(createdAt);

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

        Love love = new Love(story, user);
        love.setId(new Random().nextLong());

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
        List<Story> storiesList = new ArrayList<>();
        Page<Long> lovesList = Mockito.mock(Page.class);
        storiesList.add(story);
        List<Long> listOfLoves = new ArrayList<>();
        listOfLoves.add((long) 53412);
        listOfLoves.add((long) 1323232);
        listOfLoves.add((long) 13434);
        int total = listOfLoves.size();
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min(start + pageable.getPageSize(), total);
        List<Love> loves = new ArrayList<>();
        loves.add(love);

        List<Long> output = new ArrayList<>();
        output = listOfLoves.subList(start, end);
        PageImpl page = new PageImpl<>(output, pageable, total);

        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(loveRepository.findLoveStoryIdsByUserId(any(Long.class), isA(Pageable.class))).thenReturn(page);
        when(storyRepository.findByIdIn(any(List.class), any(Sort.class))).thenReturn(storiesList);
        when(loveRepository.findByUserIdAndStoryIdIn(any(Long.class), any(List.class))).thenReturn(loves);
        when(userRepository.findByIdIn(any(List.class))).thenReturn(users);
        when(loveRepository.countByUserId(any(Long.class))).thenReturn(topic.getId());
        Assert.assertTrue(storyService.getStoriesLovedBy("dasda", userPrincipal, 0, 10).getContent().get(0).getTitle().contains(story.getTitle()));
        Assert.assertTrue(storyService.getStoriesLovedBy("dasda", userPrincipal, 0, 10).getContent().get(0).getDescription().contains(story.getDescription()));
        Assert.assertTrue(storyService.getStoriesLovedBy("dasda", userPrincipal, 0, 10).getContent().get(0).getTopic().getDescription().contains(topic.getDescription()));
        Assert.assertTrue(storyService.getStoriesLovedBy("dasda", userPrincipal, 0, 10).getContent().get(0).getTopic().getTitle().contains(topic.getTitle()));

    }

    @Test
    public void should_return_create_method() throws Exception {
        Topic topic  = new Topic("Topic Name", "Topic Description");
        Story story = new Story("Story Title", "Story Body", "Description");
        story.setPremiumContent(true);
        story.setTopic(topic);
        User user = new User("Antek Karwasz", "karwasz123", "karwasz@gmail.com", "password");
        StoryRequest storyRequest = new StoryRequest(story.getTitle(), story.getBody(), story.getDescription());
        storyRequest.setPremiumContent(story.isPremiumContent());
        when(topicRepository.findById(any(Long.class))).thenReturn(Optional.of(topic));
        when(storyRepository.save(any(Story.class))).thenReturn(story);
        Assert.assertTrue(storyService.createStory(storyRequest, (long) 1).getTitle().contains(story.getTitle()));
        Assert.assertTrue(storyService.createStory(storyRequest, (long) 1).getBody().contains(story.getBody()));
        Assert.assertTrue(storyService.createStory(storyRequest, (long) 1).getDescription().contains(story.getDescription()));
        Assert.assertTrue(storyService.createStory(storyRequest, (long) 23).getTopic().getDescription().contains(topic.getDescription()));
        Assert.assertTrue(storyService.createStory(storyRequest, (long) 23).getTopic().getTitle().contains(topic.getTitle()));
        Assert.assertEquals(storyService.createStory(storyRequest, 12l).isPremiumContent(), true);
    }

    @Test
    public void should_return_castLoveAndGetUpdateStory_method() throws Exception {
        Instant createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        Topic topic  = new Topic("Topic Name", "Topic Description");
        topic.setCreatedBy((long) 12);
        topic.setCreatedAt(createdAt);
        topic.setUpdatedAt(createdAt);
        Story story = new Story("Story Title", "Story Body", "Description");
        story.setCreatedBy((long) 123);
        story.setId((long) 1243);
        story.setTopic(topic);
        story.setCreatedAt(createdAt);
        story.setUpdatedAt(createdAt);
        User user = new User((long ) 10020,"John Doe", "johndoe123", "joedoe@gmail.com", "password");
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        Love love = new Love(story, user);
        when(userRepository.getOne(any(Long.class))).thenReturn(user);
        when(storyRepository.findById(any(Long.class))).thenReturn(Optional.of(story));
        when(loveRepository.save(any(Love.class))).thenReturn(love);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(loveRepository.countByStoryId(any(Long.class))).thenReturn(story.getCreatedBy());
        when(loveRepository.countByStoryId(any(Long.class))).thenReturn((long) 123);
        Assert.assertTrue(storyService.castLoveAndGetUpdateStory((long) 123,userPrincipal).getBody().contains("Story Body"));
        Assert.assertTrue(storyService.castLoveAndGetUpdateStory((long) 12434324, userPrincipal).getDescription().contains(story.getDescription()));
        Assert.assertTrue(storyService.castLoveAndGetUpdateStory((long) 12434324, userPrincipal).getTitle().contains(story.getTitle()));
        Assert.assertTrue(storyService.castLoveAndGetUpdateStory((long) 43346546, userPrincipal).getTopic().getTitle().contains(topic.getTitle()));
        Assert.assertTrue(storyService.castLoveAndGetUpdateStory((long) 43346546, userPrincipal).getTopic().getDescription().contains(topic.getDescription()));
        Assert.assertEquals(createdAt, storyService.castLoveAndGetUpdateStory((long) 43346546, userPrincipal).getTopic().getcreatedAt());
        Assert.assertEquals(topic.getId(), storyService.castLoveAndGetUpdateStory((long) 43346546, userPrincipal).getTopic().getId());





    }

    @Test
    public void should_return_getStoryByTopicId_method() throws Exception {
        Instant createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        Topic topic  = new Topic("Topic Name", "Topic Description");
        topic.setCreatedAt(createdAt);
        topic.setUpdatedAt(createdAt);
        topic.setId((long) 123);
        Story story = new Story("Story Title", "Story Body", "Description");
        story.setCreatedBy((long) 233);
        story.setId((long) 233);
        story.setTopic(topic);
        story.setCreatedAt(createdAt);
        story.setUpdatedAt(createdAt);
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
        List<Story> storiesList = new ArrayList<>();
        storiesList.add(story);
        User user = new User((long ) 1223343,"Edvard More", "edvardmore123", "edvardmore@gmail.com", "password");
        UserPrincipal userPrincipal = UserPrincipal.create(user);

        PageImpl<Story> page = MockUtil.createMockPage(storiesList);
        when(loveRepository.countByStoryId(any(Long.class))).thenReturn(new Random().nextLong());
        when(topicRepository.findById((any(Long.class)))).thenReturn(Optional.of(topic));
        when(storyRepository.findByTopicId(any(Long.class), isA(Pageable.class))).thenReturn(page);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        Assert.assertEquals(1, storyService.getStoryByTopicId(topic.getId(), userPrincipal, 0, 10).getContent().size());
        Assert.assertEquals(1, storyService.getStoryByTopicId(topic.getId(), userPrincipal, 0, 10).getTotalPages());
        Assert.assertEquals(0, storyService.getStoryByTopicId(topic.getId(), userPrincipal, 0, 10).getPage());
        Assert.assertEquals(1, storyService.getStoryByTopicId(topic.getId(), userPrincipal, 0, 10).getTotalElement());
        Assert.assertTrue(storyService.getStoryByTopicId(topic.getId(), userPrincipal, 0, 10).getContent().get(0).getTitle().contains(story.getTitle()));
        Assert.assertTrue(storyService.getStoryByTopicId(topic.getId(), userPrincipal, 0, 10).getContent().get(0).getTopic().getTitle().contains(topic.getTitle()));
        Assert.assertTrue(storyService.getStoryByTopicId(topic.getId(), userPrincipal, 0, 10).getContent().get(0).getTopic().getDescription().contains(topic.getDescription()));


    }

    @Test
    public void should_return_getStoryCreatorMap() throws Exception {
        Instant createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        User user = new User();
        user.setUsername("hosu794");
        user.setName("Grzegorz Szczęsny");
        user.setEmail("grzesszesny14@gmail.com");
        user.setId((long) 12432);
        user.setUpdatedAt(createdAt);
        user.setCreatedAt(createdAt);

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        Topic topic = new Topic();
        topic.setCreatedBy(user.getId());
        topic.setCreatedAt(createdAt);
        topic.setTitle("Title of the topic");
        topic.setUpdatedAt(createdAt);
        topic.setCreatedAt(createdAt);
        topic.setDescription("Description");
        topic.setId((long) 23422434);

        Story story = new Story();
        story.setCreatedBy(user.getId());
        story.setCreatedAt(createdAt);
        story.setUpdatedAt(createdAt);
        story.setTitle("Title of the story");
        story.setDescription("Description of the story");
        story.setId((long) 233);
        story.setBody("Body");
        story.setTopic(topic);

        List<Story> stories = new ArrayList<>();
        stories.add(story);

        List<User> users  = new ArrayList<>();
        users.add(user);

        Map<Long, User> creatorMap = new HashMap<>();
        creatorMap.put(user.getId(), user);

        Mockito.when(userRepository.findByIdIn(ArgumentMatchers.any(List.class))).thenReturn(users);

        Assert.assertEquals(creatorMap.size(), storyService.getCreatorsIdsAndCreatorOfStories(stories).size());
        Assert.assertEquals(creatorMap.get(0), storyService.getCreatorsIdsAndCreatorOfStories(stories).get(0));
        Assert.assertEquals(creatorMap, storyService.getCreatorsIdsAndCreatorOfStories(stories));

    }


    @Test
    public void should_return_getStoryUserLoveMap() throws Exception {
        Instant createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        User user = new User();
        user.setUsername("hosu794");
        user.setName("Grzegorz Szczęsny");
        user.setEmail("grzesszesny14@gmail.com");
        user.setId((long) 12432);
        user.setUpdatedAt(createdAt);
        user.setCreatedAt(createdAt);

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        Topic topic = new Topic();
        topic.setCreatedBy(user.getId());
        topic.setCreatedAt(createdAt);
        topic.setTitle("Title of the topic");
        topic.setUpdatedAt(createdAt);
        topic.setCreatedAt(createdAt);
        topic.setDescription("Description");
        topic.setId((long) 23422434);

        Story story = new Story();
        story.setCreatedBy(user.getId());
        story.setCreatedAt(createdAt);
        story.setUpdatedAt(createdAt);
        story.setTitle("Title of the story");
        story.setDescription("Description of the story");
        story.setId((long) 233);
        story.setBody("Body");
        story.setTopic(topic);

        Love love1 = new Love();
        love1.setUser(user);
        love1.setStory(story);
        love1.setId((long) 1221);

        List<Love> loves = new ArrayList<>();
        loves.add(love1);

        List<Long> longs = new ArrayList<>();
        longs.add(love1.getId());

        Map<Long, Long> storyUserLoveMap = new HashMap<>();
        storyUserLoveMap.put(love1.getStory().getId(), love1.getId());

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");


        int total = loves.size();
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min(start + pageable.getPageSize(), total);
        List<Love> loveArrayList = new ArrayList<>();
        loveArrayList.add(love1);

        List<Love> output = new ArrayList<>();
        output = loveArrayList.subList(start, end);
        PageImpl page = new PageImpl<>(output, pageable, total);


        Assert.assertEquals(storyUserLoveMap.get(0), storyService.getCurrentUserStoryIds(userPrincipal, longs).get(0));


    }

    @Test
    public void should_return_deleteLove_method() throws Exception {
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
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(createdAt);

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

        Love love = new Love(story, user);
        love.setId(new Random().nextLong());

        Mockito.when(userRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(user));
        Mockito.when(storyRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(story));
        Mockito.when(loveRepository.findByUserIdAndStoryId(ArgumentMatchers.any(Long.class), ArgumentMatchers.any(Long.class))).thenReturn(love);
        Mockito.when(loveRepository.countByStoryId(ArgumentMatchers.any(Long.class))).thenReturn((long) 12);
        Assert.assertEquals(story.getBody(), storyService.deleteLoveAndGetUpdateStory(user.getId(), userPrincipal).getBody());
        Assert.assertEquals(story.getTitle(), storyService.deleteLoveAndGetUpdateStory(user.getId(), userPrincipal).getTitle());
        Assert.assertEquals(story.getDescription(), storyService.deleteLoveAndGetUpdateStory(user.getId(), userPrincipal).getDescription());
        Assert.assertEquals(user.getUsername(), storyService.deleteLoveAndGetUpdateStory(user.getId(), userPrincipal).getCreatedBy().getUsername());

    }

    @Test
    public void should_return_deleteStory() throws Exception {
        Instant createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        User user = new User();
        user.setUsername("hosu794");
        user.setName("Grzegorz Szczęsny");
        user.setEmail("grzesszesny14@gmail.com");
        user.setId((long) 12432);
        user.setUpdatedAt(createdAt);
        user.setCreatedAt(createdAt);



        UserPrincipal userPrincipal = UserPrincipal.create(user);

        Topic topic = new Topic();
        topic.setCreatedBy(user.getId());
        topic.setCreatedAt(createdAt);
        topic.setTitle("Title of the topic");
        topic.setUpdatedAt(createdAt);
        topic.setCreatedAt(createdAt);
        topic.setDescription("Description");
        topic.setId((long) 23422434);

        Story story = new Story();
        story.setCreatedBy(user.getId());
        story.setCreatedAt(createdAt);
        story.setUpdatedAt(createdAt);
        story.setTitle("Title of the story");
        story.setDescription("Description of the story");
        story.setId((long) 233);
        story.setBody("Body");
        story.setTopic(topic);

        Love love = new Love(story, user);
        love.setId(43443L);

        ApiResponse apiResponse = new ApiResponse(true, "Story deleted successful");

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(storyRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(story));
        Assert.assertEquals(apiResponse.getSuccess(), storyService.deleteStory(ArgumentMatchers.anyLong(), userPrincipal).hasBody());

        story.setCreatedBy(12343L);
        user.setId(34409545L);


        Assertions.assertThrows(BadRequestException.class, () -> {
            storyService.deleteStory(ArgumentMatchers.anyLong(), userPrincipal);
        });

    }

    @Test
    public void should_return_updateStory() throws Exception {
        Instant createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        User user = new User();
        user.setUsername("hosu794");
        user.setName("Grzegorz Szczęsny");
        user.setEmail("grzesszesny14@gmail.com");
        user.setId((long) 12432);
        user.setUpdatedAt(createdAt);
        user.setCreatedAt(createdAt);



        UserPrincipal userPrincipal = UserPrincipal.create(user);

        Topic topic = new Topic();
        topic.setCreatedBy(user.getId());
        topic.setCreatedAt(createdAt);
        topic.setTitle("Title of the topic");
        topic.setUpdatedAt(createdAt);
        topic.setCreatedAt(createdAt);
        topic.setDescription("Description");
        topic.setId((long) 23422434);

        Story story = new Story();
        story.setCreatedBy(user.getId());
        story.setCreatedAt(createdAt);
        story.setUpdatedAt(createdAt);
        story.setTitle("Title of the story");
        story.setDescription("Description of the story");
        story.setId((long) 233);
        story.setBody("Body");
        story.setTopic(topic);

        StoryRequest storyRequest = new StoryRequest("title", "body", "description");

        Story updatedStory = new Story();
        updatedStory.setCreatedBy(user.getId());
        updatedStory.setCreatedAt(createdAt);
        updatedStory.setUpdatedAt(createdAt);
        updatedStory.setTitle("Title of updated story");
        updatedStory.setDescription("Description of updated story");
        updatedStory.setId((long) 233);
        updatedStory.setBody("Body");
        updatedStory.setTopic(topic);



        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));

        Mockito.when(storyRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(story));

        Mockito.when(storyRepository.save(ArgumentMatchers.any(Story.class))).thenReturn(updatedStory);

        Mockito.when(loveRepository.countByStoryId(ArgumentMatchers.anyLong())).thenReturn(12l);


        Assert.assertEquals(storyService.updateStory(storyRequest, story.getId(), userPrincipal).getDescription(), updatedStory.getDescription());
        Assert.assertEquals(storyService.updateStory(storyRequest, story.getId(),  userPrincipal).getBody(), updatedStory.getBody());


    }




}
