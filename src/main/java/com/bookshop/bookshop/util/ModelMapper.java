package com.bookshop.bookshop.util;

import com.bookshop.bookshop.model.Story;
import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.StoryResponse;
import com.bookshop.bookshop.payload.UserSummary;

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


        return storyResponse;


    }

}
