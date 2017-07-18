package com.b1ngoos.walllikes;

import com.b1ngoos.walllikes.domain.Friend;

import java.util.List;

public interface FriendsCollection{

    void add(Friend friend);
    void remove(Friend friend);
    Friend get(int id);
    List<Friend> getList();
    int[] getIds();

}
