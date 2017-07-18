package com.b1ngoos.walllikes;

import com.b1ngoos.walllikes.domain.Friend;

import java.util.*;

public class FriendsCollectionImpl implements FriendsCollection {

    private List<Friend> friends;

    public FriendsCollectionImpl() {
        friends = new ArrayList<Friend>();
    }

    @Override
    public void add(Friend friend) {
        friends.add(friend);
    }

    @Override
    public void remove(Friend friend) {
        friends.remove(friend);
    }

    @Override
    public Friend get(int i) {
        return friends.get(i);
    }

    @Override
    public List<Friend> getList() {
        return friends;
    }

    @Override
    public int[] getIds() {

        int[] ids = new int[friends.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = get(i).getId();
        }
        return ids;
    }

}
