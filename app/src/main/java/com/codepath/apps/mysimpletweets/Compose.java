package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Compose extends AppCompatActivity {
    TextView tvuserNameOf;
    TextView screenNameOfcompose;
    ImageView ivProfileCompose;
    EditText edtBody;
    TwitterClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        //hide tolbar
        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        actionBar.hide();
        client = TwitterApplication.getRestClient();
        setUpMyView();
    }

    public void setUpMyView(){
        User user = (User) getIntent().getSerializableExtra("authUser");
        edtBody = (EditText) findViewById(R.id.edtBody);
        tvuserNameOf = (TextView) findViewById(R.id.tvuserNameOf);
        screenNameOfcompose = (TextView) findViewById(R.id.screenNameOfcompose);
        ivProfileCompose = (ImageView) findViewById(R.id.ivProfileCompose);
        tvuserNameOf.setText(user.getName());
        screenNameOfcompose.setText(String.format("@%s",user.getScreenName()));

        String myProfileImg = user.getProfileImageUrl();

        if (!TextUtils.isEmpty(myProfileImg)){
            Picasso.with(this).load(myProfileImg).into(ivProfileCompose);
        }

    }

    public void onTweet(View view){
        String status =  edtBody.getText().toString();
        Toast.makeText(this,"I tweet " ,Toast.LENGTH_LONG).show();
        client.postUpdateStatus(status, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Tweet tweet = Tweet.fromJSON(response);
                Intent i = new Intent();
                i.putExtra("tweet" , tweet);
                setResult(RESULT_OK,i);
                finish();
            }

            // on faillure
            @Override
            public void onFailure (int statusCode , Header[] headers, Throwable throwable, JSONObject errorResponse){
                Log.d("DEBUG", errorResponse.toString());
                if(throwable.getMessage().contains("resolve host")){
                    Toast.makeText(Compose.this, " no connexion internet", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void onClose(View view){
        finish();
        overridePendingTransition(0,0);
    }
}
