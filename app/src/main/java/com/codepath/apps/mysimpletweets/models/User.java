package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Julio on 7/29/2016.
 */
public class User  implements Serializable{
    private  String name;
    private  Long uid;
    private  String screenName;
    private  String profileImageUrl;

    public String getProfileImageBack() {
        return profileImageBack;
    }

    private  String profileImageBack;


    public String getName() {
        return name;
    }

    public Long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getTvTageline() {
        return tvTageline;
    }

    private  String tvTageline;

    private  int tvFollowers;

    public int getTvFollowers() {
        return tvFollowers;
    }

    public int getTvFollowing() {
        return tvFollowing;
    }

    private  int tvFollowing;

    public static User fromJSON (JSONObject json){
        User u = new User();

        try {
            u.name = json.getString("name");
            u.uid = json.getLong("id");
            u.screenName = json.getString("screen_name");
            u.tvTageline = json.getString("description");
            u.tvFollowers = json.getInt("followers_count");
            u.tvFollowing = json.getInt("friends_count");
            u.profileImageUrl = json.getString("profile_image_url");
            u.profileImageBack = json.getString("profile_background_image_url");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }
}
