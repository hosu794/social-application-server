package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.model.Story;
import com.bookshop.bookshop.model.Topic;
import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.PagedResponse;
import com.bookshop.bookshop.payload.StoryResponse;
import com.bookshop.bookshop.repository.LoveRepository;
import com.bookshop.bookshop.repository.StoryRepository;
import com.bookshop.bookshop.repository.TopicRepository;
import com.bookshop.bookshop.repository.UserRepository;
import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.service.StoryService;
import com.bookshop.bookshop.service.StoryServiceImpl;
import com.bookshop.bookshop.util.AppConstants;
import org.hibernate.query.criteria.internal.expression.SimpleCaseExpression;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StoryControllerTest {

    private StoryRepository storyRepository;
    private StoryService storyService;
    private UserRepository userRepository;
    private LoveRepository loveRepository;
    private TopicRepository topicRepository;

    @Test
    public void should_return_correct_story_by_id() throws Exception {

        storyRepository = mock(StoryRepository.class);
        userRepository = mock(UserRepository.class);
        loveRepository = mock(LoveRepository.class);
        topicRepository = mock(TopicRepository.class);
        storyService = new StoryServiceImpl(storyRepository, userRepository, loveRepository, topicRepository);


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
    public void should_return_topic_belong_to_story() throws Exception {
        storyRepository = mock(StoryRepository.class);
        userRepository = mock(UserRepository.class);
        loveRepository = mock(LoveRepository.class);
        topicRepository = mock(TopicRepository.class);
        storyService = new StoryServiceImpl(storyRepository, userRepository, loveRepository, topicRepository);


        Topic topic = new Topic();
        topic.setDescription("Topic Description");
        topic.setTitle("Topic title");
        topic.setId((long) 2);

        Story story = new Story();
        story.setCreatedBy((long) 2);
        story.setId((long) 1);
        story.setTopic(topic);
        story.setCreatedBy((long) 2);
        story.setTitle("Story Title");

        User user = new User();
        user.setUsername("hosu794");
        user.setPassword("password");
        user.setName("Grzegorz Szczęsny");
        user.setEmail("hosu794@gmail.com");
        UserPrincipal userPrincipal = UserPrincipal.create(user);

        Page<Story> stories = Mockito.mock(Page.class);

        Pageable pageable = PageRequest.of(0, 30, Sort.Direction.DESC, "createdAt");


        when(storyRepository.findByTopicId((long) 2, pageable)).thenReturn(stories);
        when(topicRepository.findById((long) 2)).thenReturn(Optional.of(topic));
        Assert.assertNotNull(storyService.getStoryByTopicId((long) 2, userPrincipal, 0, 30).getContent());
        Assert.assertNotNull(storyService.getStoryByTopicId((long) 2, userPrincipal, 0, 30).getSize());
        Assert.assertEquals(false, storyService.getStoryByTopicId((long) 2, userPrincipal, 0, 30).isLast());
        Assert.assertEquals(0, storyService.getStoryByTopicId((long) 2, userPrincipal, 0, 30).getPage());
        Assert.assertEquals(0, storyService.getStoryByTopicId((long) 2, userPrincipal, 0, 30).getSize());
        Assert.assertEquals(0, storyService.getStoryByTopicId((long) 2, userPrincipal, 0, 30).getTotalPages());
        Assert.assertNotNull(storyService.getStoryByTopicId((long) 2, userPrincipal, 0, 30).getContent());

    }

}
