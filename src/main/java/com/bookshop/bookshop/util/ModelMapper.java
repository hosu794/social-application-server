package com.bookshop.bookshop.util;

import com.bookshop.bookshop.model.Comment;
import com.bookshop.bookshop.model.Story;
import com.bookshop.bookshop.model.Topic;
import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.CommentResponse;
import com.bookshop.bookshop.payload.StoryResponse;
import com.bookshop.bookshop.payload.TopicResponse;
import com.bookshop.bookshop.payload.UserSummary;
import org.springframework.ui.Model;

import java.time.Instant;

public class ModelMapper {

    public static StoryResponse mapStoryToStoryResponse(Story story, User creator, Long userLove) {
        StoryResponse storyResponse = new StoryResponse();
        storyResponse.setId(story.getId());
        storyResponse.setBody(story.getBody());
        storyResponse.setTitle(story.getTitle());
        storyResponse.setCreationDateTime(story.getCreatedAt());
        storyResponse.setDescription(story.getDescription());
        UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
        storyResponse.setCreatedBy(creatorSummary);
        storyResponse.setTotalLoves(userLove);
        TopicResponse topicResponse = ModelMapper.mapTopicToTopicResponse(story.getTopic(), creator);
        storyResponse.setTopic(topicResponse);



        return storyResponse;


    }

    public static TopicResponse mapTopicToTopicResponse(Topic topic, User creator) {
        TopicResponse topicResponse = new TopicResponse();
        topicResponse.setId(topic.getId());
        topicResponse.setTitle(topic.getTitle());
        topicResponse.setDescription(topic.getDescription());
        UserSummary userSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
        topicResponse.setCreatedBy(userSummary);
        topicResponse.setcreatedAt(topic.getCreatedAt());

        return topicResponse;
    }

    public static CommentResponse mapCommentToCommentResponse(Comment comment, User creator, StoryResponse story) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(comment.getId());
        commentResponse.setBody(comment.getBody());
        commentResponse.setCreationDateTime(comment.getCreatedAt());
        commentResponse.setStoryResponse(story);
        UserSummary userSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
        commentResponse.setCreatedBy(userSummary);

        return commentResponse;
    }

}
