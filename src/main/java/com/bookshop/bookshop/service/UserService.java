package com.bookshop.bookshop.service;

import com.bookshop.bookshop.payload.*;
import com.bookshop.bookshop.security.UserPrincipal;
import org.springframework.stereotype.Service;

public interface UserService {

    UserSummary getCurrentUser(UserPrincipal currentUser);

    UserIdentityAvailability checkUsernameAvailability(UsernameRequest usernameRequest);

    UserIdentityAvailability checkEmailAvailability(EmailRequest emailRequest);

    UserProfile getUserProfile(String username);

    PagedResponse<StoryResponse> getStoriesCreatedBy(String username, UserPrincipal currentUser, int page, int size);

    PagedResponse<StoryResponse> getStoriesLovedBy(String username, UserPrincipal currentUser, int page, int size);

    LoveAvailability checkIsUserLovedStory(Long storyId, Long userId);

}
