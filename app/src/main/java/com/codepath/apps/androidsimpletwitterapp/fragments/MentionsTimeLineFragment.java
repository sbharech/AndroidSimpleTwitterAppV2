package com.codepath.apps.androidsimpletwitterapp.fragments;

import android.os.Bundle;
import android.view.View;

import com.codepath.apps.androidsimpletwitterapp.CurrentUser;
import com.codepath.apps.androidsimpletwitterapp.NetworkErrorDialog;
import com.codepath.apps.androidsimpletwitterapp.TwitterApplication;
import com.codepath.apps.androidsimpletwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by suraj on 25/02/15.
 */
public class MentionsTimeLineFragment extends TimeLineFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateTimeLine();
    }

    public void addNewTweet(Tweet tweet) {
        ArrayList<String> userMentionedList = tweet.getUserMentionedList();
        if (userMentionedList != null &&  CurrentUser.getInstance().getCurrentUser() != null) {
            for (String mentionedUser : userMentionedList) {
                if (CurrentUser.getInstance().getCurrentUser().getHandleName().equals(mentionedUser)) {
                    tweetList.add(0, tweet);
                    arrayAdaptor.notifyDataSetChanged();
                }
            }
        }
    }

    public void populateTimeLine(final long maxId) {
        TwitterApplication.getRestClient().getMentionsTimeLineList(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                progressLoading.setVisibility(View.GONE);
                if (maxId == 0) {
                    clearAllTweets();
                }
                addAllTweets(Tweet.getTweetList(response));
                setMaxId(tweetList.get(tweetList.size() - 1).getTweetId());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressLoading.setVisibility(View.GONE);
                NetworkErrorDialog errorDialog = new NetworkErrorDialog(getActivity());
                errorDialog.createNetworkErrorDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                progressLoading.setVisibility(View.GONE);
                NetworkErrorDialog errorDialog = new NetworkErrorDialog(getActivity());
                errorDialog.createNetworkErrorDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressLoading.setVisibility(View.GONE);
                NetworkErrorDialog errorDialog = new NetworkErrorDialog(getActivity());
                errorDialog.createNetworkErrorDialog();
            }

        }, maxId);
    }
}
