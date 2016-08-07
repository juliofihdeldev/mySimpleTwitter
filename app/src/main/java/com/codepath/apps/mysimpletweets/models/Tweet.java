package com.codepath.apps.mysimpletweets.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Julio on 7/28/2016.
 */
public class Tweet implements Serializable {

    private String body;
    private long uid;
    private String createdAt;
    private String tvSource;
    private User user;

    private ArrayList<String> photoUrls;
    private Integer retweetCount;
    private Integer favoriteCount;


    public ArrayList<String> getPhotoUrls() {
        return photoUrls;
    }

    public Integer getRetweetCount() {
        return retweetCount;
    }

    public Integer getFavoriteCount() {
        return favoriteCount;
    }
    public String getTvSource() {
        return tvSource;
    }
    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.tvSource = jsonObject.getString("source");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));

            // retweet and favorite count
            tweet.retweetCount = jsonObject.getInt("retweet_count");
            tweet.favoriteCount = jsonObject.getInt("favorite_count");

            // take tweet media if Json has entities and media
            tweet.photoUrls = new ArrayList<>();
            if (jsonObject.has("entities") && jsonObject.getJSONObject("entities").has("media")) {
                JSONArray medias = jsonObject.getJSONObject("entities").getJSONArray("media");
                for (int i = 0; i < medias.length(); i++) {
                    JSONObject media = medias.getJSONObject(i);

                    if (media.getString("type").equals("photo")) {
                        tweet.photoUrls.add(media.getString("media_url"));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    // Create de jsonArray
    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        //interate the json array
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

        }
        return tweets;
    }
}
