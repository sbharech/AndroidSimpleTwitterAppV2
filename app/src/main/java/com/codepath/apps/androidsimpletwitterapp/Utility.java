package com.codepath.apps.androidsimpletwitterapp;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by suraj on 17/02/15.
 */
public class Utility {
    public static String getRelativeTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
        try {
            String relativeTime = DateUtils.getRelativeTimeSpanString(format.parse(time).getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            relativeTime = relativeTime.replaceAll("ago", "");
            relativeTime = relativeTime.replaceAll("minute.?", "m");
            relativeTime = relativeTime.replaceAll("second.?", "s");
            relativeTime = relativeTime.replaceAll("hour.?", "h");
            relativeTime = relativeTime.replaceAll("day.?", "d");
            relativeTime = relativeTime.replaceAll("week.?", "W");
            relativeTime = relativeTime.replaceAll("year.?", "y");
            relativeTime = relativeTime.replaceAll(" ", "");
            relativeTime = relativeTime.replaceAll("in", "");

            if ("0s".equals(relativeTime))
                relativeTime = "now";
            return relativeTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getDisplayWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getDisplayHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static String coolStringFormat(int n) {
        if (n >= 0 && n < 10000)
            return String.valueOf(n);

        if (n < 10 * 1000 * 1000) {
            if (n % 1000 == 0)
                return String.valueOf(n/1000) + "k";
            n = n/100;
            double d = (double)n/10;

            return String.valueOf(d) + "k";
        }

        n = n / 1000;

        if (n >= 0 && n < 10000)
            return String.valueOf(n) + "k";


        if (n % 1000 == 0)
            return String.valueOf(n/1000) + "m";

        n = n/100;

        double d = n/10;

        return String.valueOf(d) + "m";

    }
}
