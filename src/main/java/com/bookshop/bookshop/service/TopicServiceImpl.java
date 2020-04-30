package com.bookshop.bookshop.service;

import com.bookshop.bookshop.exception.ResourceNotFoundException;
import com.bookshop.bookshop.model.Story;
import com.bookshop.bookshop.model.Topic;
import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.PagedResponse;
import com.bookshop.bookshop.payload.StoryResponse;
import com.bookshop.bookshop.payload.TopicRequest;
import com.bookshop.bookshop.payload.TopicResponse;
import com.bookshop.bookshop.repository.TopicRepository;
import com.bookshop.bookshop.repository.UserRepository;
import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.util.ModelMapper;
import com.bookshop.bookshop.util.ValidatePageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl implements TopicService {

    private static final Logger logger = LoggerFactory.getLogger(TopicServiceImpl.class);

    public TopicServiceImpl(TopicRepository topicRepository, UserRepository userRepository) {
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
    }

    public TopicServiceImpl() {}

   private TopicRepository topicRepository;
   private UserRepository userRepository;

    public PagedResponse<TopicResponse> getAllTopics(UserPrincipal currentUser, int page, int size) {
        ValidatePageUtil.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Topic> topics = topicRepository.findAll(pageable);

        if(topics.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), topics.getNumber(), topics.getSize(), topics.getTotalElements(), topics.getTotalPages(), topics.isLast());
        }

        Map<Long, User> creatorMap = getTopicCreatorMap(topics.getContent());

        List<TopicResponse> topicResponses = topics.map(topic -> {

            return ModelMapper.mapTopicToTopicResponse(topic, creatorMap.get(topic.getCreatedBy()));
        }).getContent();

        return new PagedResponse<>(topicResponses, topics.getNumber(), topics.getSize(), topics.getTotalElements(), topics.getTotalPages(), topics.isLast());
    }

    public PagedResponse<TopicResponse> getTopicByCreatedBy(String username, UserPrincipal currentUser, int page, int size) {
        ValidatePageUtil.validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User","username", username));


        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Topic> topics = topicRepository.findByCreatedBy(user.getId(), pageable);

        if(topics.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), topics.getNumber(), topics.getSize(), topics.getTotalElements(), topics.getTotalPages(), topics.isLast());
        }

        List<TopicResponse> topicResponses = topics.map(topic -> {

            return ModelMapper.mapTopicToTopicResponse(topic, user);
        }).getContent();

        return new PagedResponse<>(topicResponses, topics.getNumber(), topics.getSize(), topics.getTotalElements(), topics.getTotalPages(), topics.isLast());

    }

    public Topic createTopic(TopicRequest topicRequest) {
        Topic topic = new Topic();
        topic.setTitle(topicRequest.getTitle());
        topic.setDescription(topicRequest.getDescription());

        return topicRepository.save(topic);
    }

    public TopicResponse getTopicById(Long topicId, UserPrincipal currentUser) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "id", topicId));

        User creator = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User",  "id", currentUser.getId()));

        return ModelMapper.mapTopicToTopicResponse(topic, creator);
    }

    //Retrieve Topic Creator details of the given list of topics
    public Map<Long, User> getTopicCreatorMap(List<Topic> topics) {


        List<Long> creatorIds = topics.stream()
                .map(Topic::getCreatedBy)
                .distinct()
                .collect(Collectors.toList());

        List<User> creators = userRepository.findByIdIn(creatorIds);

        Map<Long, User> creatorMap = creators.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return creatorMap;

    }

}
