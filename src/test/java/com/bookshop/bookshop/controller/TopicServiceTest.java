package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.model.Topic;
import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.TopicRequest;
import com.bookshop.bookshop.repository.TopicRepository;
import com.bookshop.bookshop.repository.UserRepository;
import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.service.TopicService;
import com.bookshop.bookshop.service.implementation.TopicServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TopicServiceTest {


    UserRepository userRepository = Mockito.mock(UserRepository.class);
    TopicRepository topicRepository = Mockito.mock(TopicRepository.class);
    TopicService topicService = new TopicServiceImpl(topicRepository, userRepository);

    @Test
    public void should_return_getAllTopics_method() throws Exception {
        Instant createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        User user = new User();
        user.setId((long) 12);
        user.setUsername("hosu794");
        user.setPassword("password");
        user.setName("Grzegorz Szczęsny");
        user.setEmail("hosu794@gmail.com");
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(createdAt);
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        List<User> users = new ArrayList<>();
        users.add(user);


        Topic topic1 = new Topic();
        topic1.setId((long) 12);
        topic1.setTitle("New Title");
        topic1.setDescription("New Description");
        topic1.setCreatedAt(createdAt);
        topic1.setUpdatedAt(createdAt);
        topic1.setCreatedBy(user.getId());

        List<Topic> topics = new ArrayList<>();
        topics.add(topic1);

        Pageable pageable = PageRequest.of(0, 30 , Sort.Direction.DESC, "createdAt");
        PageImpl<Topic> topicPage = createMockPage(topics);

        Mockito.when(userRepository.findByIdIn(ArgumentMatchers.any(List.class))).thenReturn(users);
        Mockito.when(topicRepository.findAll(ArgumentMatchers.isA(Pageable.class))).thenReturn(topicPage);
        Assert.assertTrue(topicService.getAllTopics(userPrincipal, 0, 30).getContent().get(0).getDescription().contains(topic1.getDescription()));
        Assert.assertTrue(topicService.getAllTopics(userPrincipal, 0, 30).getContent().get(0).getTitle().contains(topic1.getTitle()));
        Assert.assertEquals(1, topicService.getAllTopics(userPrincipal, 0, 30).getContent().size());
        Assert.assertEquals(0, topicService.getAllTopics(userPrincipal, 0, 30).getPage());
        Assert.assertEquals(1, topicService.getAllTopics(userPrincipal, 0, 30).getTotalElement());
        Assert.assertEquals(1, topicService.getAllTopics(userPrincipal, 0, 30).getTotalPages());
        Assert.assertEquals(true, topicService.getAllTopics(userPrincipal, 0, 30).isLast());
        Assert.assertEquals(30, topicService.getAllTopics(userPrincipal, 0, 30).getSize());
    }

    @Test
    public void should_return_getTopicByCreatedBy() throws Exception {
        Instant createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        User user = new User();
        user.setId((long) 12);
        user.setUsername("hosu794");
        user.setPassword("password");
        user.setName("Grzegorz Szczęsny");
        user.setEmail("hosu794@gmail.com");
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(createdAt);
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        List<User> users = new ArrayList<>();
        users.add(user);


        Topic topic1 = new Topic();
        topic1.setId((long) 12);
        topic1.setTitle("New Title");
        topic1.setDescription("New Description");
        topic1.setCreatedAt(createdAt);
        topic1.setUpdatedAt(createdAt);
        topic1.setCreatedBy(user.getId());

        List<Topic> topics = new ArrayList<>();
        topics.add(topic1);

        Pageable pageable = PageRequest.of(0, 30 , Sort.Direction.DESC, "createdAt");
        PageImpl<Topic> topicPage = createMockPage(topics);


        Mockito.when(topicRepository.findByCreatedBy(ArgumentMatchers.any(Long.class), ArgumentMatchers.isA(Pageable.class))).thenReturn(topicPage);
        Mockito.when(userRepository.findByUsername(ArgumentMatchers.any(String.class))).thenReturn(Optional.of(user));
        Assert.assertTrue(topicService.getTopicByCreatedBy(user.getUsername(), userPrincipal, 0, 30).getContent().get(0).getTitle().contains(topic1.getTitle()));
        Assert.assertTrue(topicService.getTopicByCreatedBy(user.getUsername(), userPrincipal, 0, 30).getContent().get(0).getDescription().contains(topic1.getDescription()));
        Assert.assertEquals(0, topicService.getTopicByCreatedBy(user.getUsername(), userPrincipal, 0, 30).getPage());
    }

    @Test
    public void should_return_getTopicById() throws Exception {
        Instant createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        User user = new User();
        user.setId((long) 12);
        user.setUsername("hosu794");
        user.setPassword("password");
        user.setName("Grzegorz Szczęsny");
        user.setEmail("hosu794@gmail.com");
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(createdAt);
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        List<User> users = new ArrayList<>();
        users.add(user);


        Topic topic1 = new Topic();
        topic1.setId((long) 12);
        topic1.setTitle("New Title");
        topic1.setDescription("New Description");
        topic1.setCreatedAt(createdAt);
        topic1.setUpdatedAt(createdAt);
        topic1.setCreatedBy(user.getId());

        Mockito.when(topicRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(topic1));
        Mockito.when(userRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(user));

        Assert.assertTrue(topicService.getTopicById(user.getId(), userPrincipal).getDescription().contains(topic1.getDescription()));
        Assert.assertTrue(topicService.getTopicById(user.getId(), userPrincipal).getTitle().contains(topic1.getTitle()));
        Assert.assertNotNull(topicService.getTopicById(user.getId(), userPrincipal).getCreatedBy());
    }

    @Test
    public void should_return_createTopic() throws Exception {
        Instant createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();
        User user = new User();
        user.setId((long) 12);
        user.setUsername("hosu794");
        user.setPassword("password");
        user.setName("Grzegorz Szczęsny");
        user.setEmail("hosu794@gmail.com");
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(createdAt);
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        List<User> users = new ArrayList<>();
        users.add(user);


        Topic topic1 = new Topic();
        topic1.setId((long) 12);
        topic1.setTitle("New Title");
        topic1.setDescription("New Description");

        TopicRequest topicRequest = new TopicRequest();
        topicRequest.setDescription(topic1.getDescription());
        topicRequest.setTitle(topic1.getTitle());

        Mockito.when(topicRepository.save(ArgumentMatchers.any(Topic.class))).thenReturn(topic1);

        Assert.assertTrue(topicService.createTopic(topicRequest).getTitle().contains(topic1.getTitle()));
        Assert.assertTrue(topicService.createTopic(topicRequest).getDescription().contains(topic1.getDescription()));
        Assert.assertNotNull(topicService.createTopic(topicRequest));
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

        List<Topic> topics = new ArrayList<>();
        topics.add(topic);

        List<User> users  = new ArrayList<>();
        users.add(user);

        Map<Long, User> creatorMap = new HashMap<>();
        creatorMap.put(user.getId(), user);

        Mockito.when(userRepository.findByIdIn(ArgumentMatchers.any(List.class))).thenReturn(users);

        Assert.assertEquals(creatorMap.size(), topicService.getTopicCreatorMap(topics).size());
        Assert.assertEquals(creatorMap.get(0), topicService.getTopicCreatorMap(topics).get(0));
        Assert.assertEquals(creatorMap, topicService.getTopicCreatorMap(topics));


    }


    private PageImpl createMockPage(List<Topic> list) {
        PageRequest pageRequest = PageRequest.of(0, 30, Sort.Direction.DESC, "createdAt");
        int total = list.size();
        int start = Math.toIntExact(pageRequest.getOffset());
        int end = Math.min(start + pageRequest.getPageSize(), total);

        List<Topic> output = new ArrayList<>();

        if(start <= end) {
            output = list.subList(start, end);
        }

        return new PageImpl<>(output, pageRequest, total);
    }


}
