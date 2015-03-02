package com.codepath.apps.androidsimpletwitterapp.fragments;

import android.os.Bundle;
import android.view.View;

import com.codepath.apps.androidsimpletwitterapp.NetworkErrorDialog;
import com.codepath.apps.androidsimpletwitterapp.TwitterApplication;
import com.codepath.apps.androidsimpletwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by suraj on 25/02/15.
 */
public class UserTimeLineFragment extends TimeLineFragment {
    String userName = null;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static UserTimeLineFragment getNewInstance(String userName) {
        UserTimeLineFragment userTimeLineFragment = new UserTimeLineFragment();
        Bundle args = new Bundle();
        args.putString("user_name", userName);
        userTimeLineFragment.setArguments(args);
        return userTimeLineFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        populateTimeLine();
    }

    public void populateTimeLine(final long maxId) {
        TwitterApplication.getRestClient().getUserTimeLine(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                progressLoading.setVisibility(View.GONE);
                if (maxId == 0) {
                    clearAllTweets();
                }
                addAllTweets(Tweet.getTweetList(response));
                if (tweetList.size() > 0)
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

        }, getArguments().getString("user_name"), maxId);
    }
}
