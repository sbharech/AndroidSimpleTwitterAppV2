package com.codepath.apps.androidsimpletwitterapp.fragments;

import android.os.Bundle;
import android.view.View;

import com.codepath.apps.androidsimpletwitterapp.NetworkErrorDialog;
import com.codepath.apps.androidsimpletwitterapp.TwitterApplication;
import com.codepath.apps.androidsimpletwitterapp.models.Tweet;
import com.codepath.apps.androidsimpletwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by suraj on 27/02/15.
 */
public class FollowingUserListFragment extends UserListFragment {

    public static FollowingUserListFragment getNewInstance(String userName) {
        FollowingUserListFragment followingUserListFragment = new FollowingUserListFragment();
        Bundle args = new Bundle();
        args.putString("user_name", userName);
        followingUserListFragment.setArguments(args);
        return followingUserListFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateUserList();
    }

    public void populateUserList(final long maxId) {
        TwitterApplication.getRestClient().getFollowingsList(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressLoading.setVisibility(View.GONE);
                try {
                    addAllUsers(User.getUsersList(response.getJSONArray("users")));
                    setMaxId(response.getLong("next_cursor"));
                } catch (JSONException e) {
                    e.printStackTrace();;
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

        }, getArguments().getString("user_name"), maxId);
    }
}
