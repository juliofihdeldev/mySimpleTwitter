package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    //declaration de mes  variable
    private SwipeRefreshLayout swipeContainer;
    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;
    User authUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
        android.R.color.holo_green_light,
        android.R.color.holo_orange_light,
        android.R.color.holo_red_light);

        // getion de tolbar

        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle(""); // set the top title
        String title = actionBar.getTitle().toString(); // get the title
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_action_icon_twitter);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // initialiser mes variables
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(this, tweets);
        lvTweets.setAdapter(aTweets);
        client = TwitterApplication.getRestClient();

        //appelation du fonction populatetimeline
        populateTimeline(Long.parseLong("0"));

        //apel au fontion fetch user
        fetchAuthUser();

        //gestion de handless scrool
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // find the oldest tweet id
                long oldestId = getOldestTweetId();

                // pocpulate timeline with tweets before the tweet with the oldest Id
                populateTimeline(oldestId);
                return true;
            }
        });
    }
    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        client.getHomeTimeline(0, new JsonHttpResponseHandler() {
            public void onSuccess(JSONArray json) {
                //populateTimeline(Long.parseLong("0"));
                swipeContainer.setRefreshing(false);
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        });
    }

    public void fetchAuthUser() {
        client.getAuthUser(new JsonHttpResponseHandler() {
            // on success
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.d("DEBUG", json.toString());
                //Deserialize json
                //Create model and add then to the adapter
                //Load the modal data intolistview
                authUser = User.fromJSON(json);
            }

            //on fail
            public void onFail(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponce) {
                Log.d("DEBUG", errorResponce.toString());
            }
        });
    }


    public void populateTimeline(Long oldestId) {
        client.getHomeTimeline(oldestId, new JsonHttpResponseHandler() {
            // on success
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("DEBUG", json.toString());
                //Deserialize json
                //Create model and add then to the adapter
                //Load the modal data intolistview
                aTweets.addAll(Tweet.fromJSONArray(json));
            }

            //on fail
            public void onFail(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponce) {
                Log.d("DEBUG", errorResponce.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.compose:
                onCompose();
                return true;
            /*case R.id.miProfile:
                showProfileView();
                return true;
                */
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onCompose() {
        // handle click here
        Intent i = new Intent(this, Compose.class);
        i.putExtra("authUser", authUser);
        startActivityForResult(i, 100);
    }

    // get oldest tweet id in the ArrayList tweets to search for other tweets before
    // this last one
    public Long getOldestTweetId() {
        // if we want to sort the tweets first
        /*Collections.sort(tweets, new Comparator<Tweet>(){
            public int compare(Tweet t1, Tweet t2) {
                if(t1.getUid() > t2.getUid())
                    return 1;
                else if(t1.getUid() < t2.getUid())
                    return -1;
                else
                    return 0;
            }
        });*/
        // tweets are already sort by Id in the ArrayList, so the last tweets in the
        return tweets.get(tweets.size() - 1).getUid();
    }

}
