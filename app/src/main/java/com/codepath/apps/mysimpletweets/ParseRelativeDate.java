package com.codepath.apps.mysimpletweets;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Julio on 7/30/2016.
 */

public class ParseRelativeDate {
    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014")
    // from string like output like 3m, 8h, 3d
    public static String getAbbreviatedTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
        try {

            long nowMs = System.currentTimeMillis();
            long thenMs = sf.parse(rawJsonDate).getTime();
            // Calculate difference in milliseconds
            long diff = nowMs - thenMs;

            // Calculate difference in seconds
            long diffSeconds = diff / 1000;
            long diffMinutes = diff / (60 * 1000);
            long diffHours = diff / (60 * 60 * 1000);
            long diffDays = diff / (24 * 60 * 60 * 1000);
            long diffMonth = diff / (24 * 60 * 60 * 1000 * 31);
            long diffYear = diff / (24 * 60 * 60 * 1000 * 31 * 12);
            
            if (diffSeconds < 60) {
                return diffSeconds + "s";

            } else if (diffMinutes < 60) {
                return diffMinutes + "m";

            } else if (diffHours < 24) {
                return diffHours + "h";

            } else if (diffDays < 7) {
                return diffDays + "d";
            }
            else if (diffMonth < 31) {
                return diffMonth + " Month";
            }
            else if (diffYear < 12) {
            return diffYear + " Yeah";
        }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sf.format(sf.getCalendar().getTime());
    }
}