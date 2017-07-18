package com.b1ngoos.walllikes;

import com.b1ngoos.walllikes.tool.TokenTool;
import com.b1ngoos.walllikes.domain.Friend;
import com.b1ngoos.walllikes.domain.UserLike;
import com.b1ngoos.walllikes.net.HttpConnectionAgent;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

public class VkToolsImpl implements VkTools{

    private URIBuilder uriBuilder = new URIBuilder();

    @Override
    public FriendsCollection getFriends(int id){

        HttpResponse response = getFriendsHttpResponse(id);

        if (response.getStatusLine().getStatusCode() == 200) {
            StringWriter content = getContent(response);

            FriendsCollection friends = parseJsonToFriendList(content);
            if (friends != null) return friends;
        }
        return new FriendsCollectionImpl();
    }

    public Set<UserLike> getWallLikes(int id) {
        return Collections.emptySet();
    }

    @Override
    public Set<UserLike> getWallPosts(int id){

        HttpResponse response = getWallPostHttpResponse(id);

        if (response.getStatusLine().getStatusCode() != 200) return Collections.emptySet();

        StringWriter content = getContent(response);

        return parseJsonToWallPost(content);
    }

    private JSONArray parseContentToJsonArr(StringWriter content) {
        JSONParser parser   = new JSONParser();
        try {
            JSONObject jsonResp = (JSONObject) parser.parse(content.toString());
            JSONArray jsonList = (JSONArray) jsonResp.get("response");
            return jsonList;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Set<UserLike> parseJsonToWallPost(StringWriter content) {
        Set<UserLike> userLikes = new HashSet<UserLike>();

            JSONArray jsonList = parseContentToJsonArr(content);

            for (int i=1; i < jsonList.size(); i++) {

                JSONObject object = (JSONObject) jsonList.get(i);
                HttpResponse response = getLikesHttpResponse(
                        Integer.parseInt(object.get("to_id").toString()),
                        Integer.parseInt(object.get("id").toString()),
                        object.get("post_type").toString()
                );

                if (response.getStatusLine().getStatusCode() != 200) return null;

                calculateLikes(userLikes, getContent(response).toString());
            }
        return userLikes;
    }

    private void calculateLikes(Set<UserLike> userLikes, String cont) {

        int firstBracket = cont.indexOf("[") + 1;
        int secondBracket = cont.indexOf("]");

        if(firstBracket == secondBracket)
            return;

        String[] ids = cont.substring(firstBracket, secondBracket).split(",");

        for(String id: ids) {
            UserLike userLike = new UserLike(Integer.parseInt(id));
            if(!userLikes.contains(userLike)) {
                userLikes.add(userLike);
            }
        }
    }

    private StringWriter getContent(HttpResponse response) {
        StringWriter content;
        content = new StringWriter();
        try {
            IOUtils.copy(response.getEntity().getContent(), content);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return content;
    }

    private String parseJsonToName(StringWriter content) {
        JSONArray jsonList = parseContentToJsonArr(content);
        JSONObject object = (JSONObject) jsonList.get(0);
        return object.get("first_name").toString() + " " + object.get("last_name").toString();
    }

    private FriendsCollection parseJsonToFriendList(StringWriter content) {
        JSONArray jsonList = parseContentToJsonArr(content);
        FriendsCollection friends = new FriendsCollectionImpl();
        for (int i=1; i < jsonList.size(); i++) {
            parseJsonToFriend((JSONObject) jsonList.get(i), friends);
        }
        return friends;
    }


    private void parseJsonToFriend(JSONObject object, FriendsCollection friends) {
        Friend friend = new Friend();
        friend.setId(Integer.parseInt(object.get("uid").toString()));
        friend.setFirstName(object.get("first_name").toString());
        friend.setLastname(object.get("last_name").toString());
        System.out.println(friend);
        friends.add(friend);
    }

    private HttpResponse getWallPostHttpResponse(Integer id) {
        uriBuilder.setScheme("https").setHost("api.vk.com").setPath("/method/wall.get").setParameters()
                .setParameter("count", "10")
                .setParameter("owner_id", id.toString())
                .setParameter("access_token", TokenTool.getToken())
                .setParameter("offset", "0");
        return HttpConnectionAgent.connectResponse(uriBuilder);
    }

    private HttpResponse getLikesHttpResponse(Integer owner_id, Integer id, String type) {
        uriBuilder.setScheme("https").setHost("api.vk.com").setPath("/method/likes.getList").setParameters()
                .setParameter("owner_id", owner_id.toString())
                .setParameter("item_id", id.toString())
                .setParameter("access_token", TokenTool.getToken())
                .setParameter("type", "post")
                .setParameter("name_case", "nom");
        return HttpConnectionAgent.connectResponse(uriBuilder);
    }

    private HttpResponse getFriendsHttpResponse(Integer id) {
        uriBuilder.setScheme("https").setHost("api.vk.com").setPath("/method/friends.get").setParameters()
                .setParameter("count", "5")
                .setParameter("user_id", id.toString())
                .setParameter("access_token", TokenTool.getToken())
                .setParameter("offset", "0")
                .setParameter("fields", "uid")
                .setParameter("name_case", "nom");
        return HttpConnectionAgent.connectResponse(uriBuilder);
    }

    private HttpResponse getNameHttpResponse(Integer id) {
        uriBuilder.setScheme("https").setHost("api.vk.com").setPath("/method/users.get").setParameters()
                .setParameter("user_ids", id.toString())
                .setParameter("access_token", TokenTool.getToken())
                .setParameter("name_case", "nom");
        return HttpConnectionAgent.connectResponse(uriBuilder);
    }

    @Override
    public String getNameById(int id) {
        HttpResponse response = getNameHttpResponse(id);
        if (response.getStatusLine().getStatusCode() == 200) {
            StringWriter content = getContent(response);
            return parseJsonToName(content);
        }
        return "";
    }
}
