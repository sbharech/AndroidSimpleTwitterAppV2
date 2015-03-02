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
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by suraj on 27/02/15.
 */
public class SearchQueryTimeLineFragment  extends TimeLineFragment {

    public static SearchQueryTimeLineFragment getNewInstance(String query) {
        SearchQueryTimeLineFragment queryTimeLineFragment = new SearchQueryTimeLineFragment();
        Bundle args = new Bundle();
        args.putString("query", query);
        queryTimeLineFragment.setArguments(args);
        return queryTimeLineFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        populateTimeLine();
    }

    public void populateTimeLine(final long maxId) {
        TwitterApplication.getRestClient().getSearchQueryList(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressLoading.setVisibility(View.GONE);
                try {
                    if (maxId == 0) {
                        clearAllTweets();
                    }
                    JSONArray result = response.getJSONArray("statuses");
                    Log.i("Fav time list", response.toString());
                    addAllTweets(Tweet.getTweetList(result));
                    if (tweetList.size() > 0)
                        setMaxId(tweetList.get(tweetList.size() - 1).getTweetId());
                } catch (JSONException e) {

                }
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

        }, getArguments().getString("query"), maxId);
    }
}
