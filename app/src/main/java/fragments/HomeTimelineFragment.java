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

public class HomeTimelineFragment extends TweetsListFragment {
    private TwitterClient client;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //recuper le rest api de twitter
        client = TwitterApplication.getRestClient();
        //appelation du fonction populatetimeline
        populateTimeline();
    }
    public void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
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
