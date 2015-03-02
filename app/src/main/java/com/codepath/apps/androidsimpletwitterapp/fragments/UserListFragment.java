package com.codepath.apps.androidsimpletwitterapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.codepath.apps.androidsimpletwitterapp.EndlessScrollListener;
import com.codepath.apps.androidsimpletwitterapp.R;
import com.codepath.apps.androidsimpletwitterapp.TweetArrayAdapter;
import com.codepath.apps.androidsimpletwitterapp.TweetMessageActivity;
import com.codepath.apps.androidsimpletwitterapp.UserArrayAdapter;
import com.codepath.apps.androidsimpletwitterapp.models.Tweet;
import com.codepath.apps.androidsimpletwitterapp.models.User;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by suraj on 27/02/15.
 */
public class UserListFragment extends Fragment {
    ArrayList<User> usersList;
    UserArrayAdapter userArrayAdapter;
    ProgressBar progressLoading;
    ListView lvUsersList;
    View fragmentView;

    long maxId = 0;

    void setMaxId(long maxId) {
        this.maxId = maxId;
    }


    public void populateUserList() {
        populateUserList(-1);
    }

    public void populateUserList(final long maxId) {

    }


    class CustomEndlessScrollListener extends EndlessScrollListener {
        public CustomEndlessScrollListener(int visibleThreshold, int itemPerPage) {
            super(visibleThreshold, itemPerPage);
        }

        @Override
        public void onLoadMore(int page, int totalItemsCount) {
            progressLoading.setVisibility(View.VISIBLE);
            populateUserList(maxId - 1);
        }
    }

    void init() {
        lvUsersList = (ListView) fragmentView.findViewById(R.id.lvUsersList);
        lvUsersList.setOnScrollListener(new CustomEndlessScrollListener(5, 25));
        progressLoading = (ProgressBar) fragmentView.findViewById(R.id.pbLoading);
        lvUsersList.setAdapter(userArrayAdapter);
    }


    public void addAllUsers(List<User> usersList) {
        userArrayAdapter.addAll(usersList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_userlist, container, false);
        init();
        return fragmentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersList = new ArrayList<User>();
        userArrayAdapter = new UserArrayAdapter(getActivity(), usersList);
        //arrayAdaptor.addAll(Tweet.getAllSavedTweet());
    }
}

