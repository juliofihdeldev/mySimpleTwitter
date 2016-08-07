package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import fragments.userTimelineFragment;

public class Profile extends AppCompatActivity {
    TwitterClient client;
    User authUser;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle("Profil"); // set the top title
        String title = actionBar.getTitle().toString(); // get the title
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.ic_action_user);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // get the screenName from actvity that he launch it
        final String screenName = getIntent().getStringExtra("screen_name");
        //Create the user Timeline fragment
        userTimelineFragment fragmentUserTimeLine = userTimelineFragment.newInstance(screenName);
        //display the fragment with the activity (dynamically)
        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeLine);
            ft.commit();
        }
        client = TwitterApplication.getRestClient();

        authUser = (User) getIntent().getSerializableExtra("authUser");
        if (authUser != null) {
            user = authUser;
            getSupportActionBar().setTitle(user.getScreenName());
            populateProfileHeader(user);

        } else {
            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // super.onSuccess(statusCode, headers, response);
                    user = User.fromJSON(response);
                    //my users account information
                    getSupportActionBar().setTitle(user.getScreenName());
                    populateProfileHeader(user);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        }
    }

    public void populateProfileHeader(User user) {

        ImageView profileImageBack = (ImageView) findViewById(R.id.profileImageBack);
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView screenName = (TextView) findViewById(R.id.screenName);
        TextView tvTageline = (TextView) findViewById(R.id.tvTageline);
        ImageView profileImageUrl = (ImageView) findViewById(R.id.ivProfileImage);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);

        tvFollowers.setText(String.valueOf(user.getTvFollowers() + " FOLLOWERS"));
        tvFollowing.setText(String.valueOf(user.getTvFollowing() + " FOLLOWING"));
        tvName.setText(user.getName());
        tvTageline.setText(user.getTvTageline());
        screenName.setText(String.format("@%s", user.getScreenName()));
        //Gestion de image
        String myProfileImg = user.getProfileImageUrl();
        Picasso.with(this).load(myProfileImg).into(profileImageUrl);
        // gestion du banner
        String banner = user.getProfileImageBack();
        Picasso.with(this).load(banner).into(profileImageBack);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(Profile.this, TimelineActivity.class);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
