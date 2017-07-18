package com.b1ngoos.walllikes;

import com.b1ngoos.walllikes.domain.UserLike;

import java.util.Set;

public interface VkTools {

    FriendsCollection getFriends(int id);
    String getNameById(int id);
    Set<UserLike> getWallPosts(int id);

}
