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

public interface TopicService {

    public PagedResponse<TopicResponse> getAllTopics(UserPrincipal currentUser, int page, int size);

    public PagedResponse<TopicResponse> getTopicByCreatedBy(String username, UserPrincipal currentUser, int page, int size);

    public Topic createTopic(TopicRequest topicRequest);

    public TopicResponse getTopicById(Long topicId, UserPrincipal currentUser);

    public Map<Long, User> getTopicCreatorMap(List<Topic> topics);


}
