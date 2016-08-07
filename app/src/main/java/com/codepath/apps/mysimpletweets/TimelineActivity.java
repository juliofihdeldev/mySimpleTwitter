package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import fragments.HomeTimelineFragment;
import fragments.MentionsTimelineFragment;

//import android.support.v4.widget.SwipeRefreshLayout;

//import android.support.v4.widget.SwipeRefreshLayout;

public class TimelineActivity extends AppCompatActivity {
    User authUser;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle(""); // set the top title
        String title = actionBar.getTitle().toString(); // get the title
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_action_icon_twitter);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        client = TwitterApplication.getRestClient();


        // run a background job and once complete
        //apel au fontion fetch user
        fetchAuthUser();

        // get the view pager
        ViewPager vpPage = (ViewPager) findViewById(R.id.viewpager);
        //et the view adapter for the view pager
        vpPage.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        //find the sliding tabstrip
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(vpPage);
    }

    public void fetchAuthUser() {
        client.getAuthUser(new JsonHttpResponseHandler() {
            // on success
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                //Log.d("DEBUG", json.toString());
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

    //Creer my adapter class
    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionsTimelineFragment();
            } else {
                return null;
            }
        }

        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
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
            case R.id.profil:
                onProfileView();
                return true;

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

    public void onProfileView() {
        // handle click here
        Intent i = new Intent(this, Profile.class);
        //i.putExtra("authUser", authUser);
        i.putExtra("authUser", authUser);
        startActivity(i);

    }

}
