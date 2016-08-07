package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Julio on 8/6/2016.
 */
public class userTimelineFragment extends TweetsListFragment {
    private TwitterClient client;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //recuper le rest api de twitter
        client = TwitterApplication.getRestClient();
        //appelation du fonction populatetimeline
        populateTimeline();
    }
    // Creates a new fragment given an int and title
    // DemoFragment.newInstance("user fragment");
    public static userTimelineFragment newInstance(String screen_name) {
        userTimelineFragment userFragment = new userTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        userFragment.setArguments(args);
        return userFragment;
    }

    public void populateTimeline() {
        String screen_name = getArguments().getString("screen_name");
        client.getUserTimeline(screen_name, new JsonHttpResponseHandler(){
            // on success
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                //Deserialize json
                //Create model and add then to the adapter
                //Load the modal data intolistview
                addAll(Tweet.fromJSONArray(json));
            }
            //on fail
            public void onFail(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponce) {
                Log.d("DEBUG", errorResponce.toString());
            }
        });
    }
}
