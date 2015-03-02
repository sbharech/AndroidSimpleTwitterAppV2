package com.codepath.apps.androidsimpletwitterapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.apps.androidsimpletwitterapp.NetworkErrorDialog;
import com.codepath.apps.androidsimpletwitterapp.TwitterApplication;
import com.codepath.apps.androidsimpletwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by suraj on 27/02/15.
 */
public class FavoriteTimeLineFragment extends TimeLineFragment {

    public static FavoriteTimeLineFragment getNewInstance(String userName) {
        FavoriteTimeLineFragment favTimeLineFragment = new FavoriteTimeLineFragment();
        Bundle args = new Bundle();
        args.putString("user_name", userName);
        favTimeLineFragment.setArguments(args);
        return favTimeLineFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        populateTimeLine();
    }

    public void populateTimeLine(final long maxId) {
        TwitterApplication.getRestClient().getFavoriteTimeLineList(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                progressLoading.setVisibility(View.GONE);
                if (maxId == 0) {
                    clearAllTweets();
                }
                Log.i("Fav time list", response.toString());
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
