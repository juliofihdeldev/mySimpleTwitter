package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Julio on 7/29/2016.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet>{

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }
    @Override
    // applied de the viewholder pattern
    public View getView(int position , View convertView, ViewGroup parent){
        //get the tweet
        Tweet tweet = getItem(position);
        //find or inflate the template

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView userHash = (TextView) convertView.findViewById(R.id.userHash);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBoby);
        TextView tvCreateAt = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvSource = (TextView) convertView.findViewById(R.id.tvSource);

        //populate data into the template

        tvUserName.setText(tweet.getUser().getName());

        //tvSource.setText(tweet.getTvSource());

        String formattedText = tweet.getTvSource();
        tvSource.setText(Html.fromHtml(formattedText));

        userHash.setText(String.format("@%s",tweet.getUser().getScreenName()));

        tvBody.setText(tweet.getBody());

        String timeAgo = ParseRelativeDate.getAbbreviatedTimeAgo(tweet.getCreatedAt());
        tvCreateAt.setText(timeAgo);

        ivProfileImage.setImageResource(android.R.color.transparent);

        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        //return the view to be insert into the list
        return convertView;

    }
}
