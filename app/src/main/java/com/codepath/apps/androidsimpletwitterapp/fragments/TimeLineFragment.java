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
import com.codepath.apps.androidsimpletwitterapp.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class TimeLineFragment extends Fragment implements TweetArrayAdapter.TweetValueChangeListener {
    public final int REQUEST_MESSAGE_DETAIL = 0;

    ArrayList<Tweet> tweetList;
    TweetArrayAdapter arrayAdaptor;
    PullToRefreshLayout pullToRefreshLayout;
    ProgressBar progressLoading;
    int positionSelected = 0;
    View viewSelected = null;
    ListView lvTweetList;
    View fragmentView;

    public interface ValueChangeListener {
        void notifyValueChange(Tweet tweet, Fragment fragment);
    }

    protected String currentUser = null;

    long maxId = 0;

    void setMaxId(long maxId)
    {
        this.maxId = maxId;
    }

    public void setCurrentUser(String user) {
        this.currentUser = user;
    }

    public void populateTimeLine() {
        populateTimeLine(0);
    }

    public void populateTimeLine(final long maxId) {

    }

    @Override
    public void notifyValueChange(Tweet tweet) {
        try {
            ValueChangeListener l = (ValueChangeListener) getActivity();
            l.notifyValueChange(tweet, this);
        } catch (ClassCastException e) {

        }
    }

    public void updateTweetInfo(Tweet tweet) {
        int i = 0;

        for (Tweet tweet1 : tweetList) {
            if (tweet.getTweetId() == tweet1.getTweetId()) {
                tweetList.set(i, tweet);

                View v = lvTweetList.getChildAt(i - lvTweetList.getFirstVisiblePosition());

                if (v != null) {
                    arrayAdaptor.updateView(getActivity(), v, tweet);
                }

                break;
            }

            ++i;
        }

    }


    class CustomEndlessScrollListener extends EndlessScrollListener {
        public CustomEndlessScrollListener(int visibleThreshold, int itemPerPage) {
            super(visibleThreshold, itemPerPage);
        }

        @Override
        public void onLoadMore(int page, int totalItemsCount) {
            progressLoading.setVisibility(View.VISIBLE);
            populateTimeLine(maxId - 1);
        }
    }

    void init() {
        pullToRefreshLayout = (PullToRefreshLayout) fragmentView.findViewById(R.id.ptrTweetListLayout);
        lvTweetList = (ListView) fragmentView.findViewById(R.id.lvTweetList);
        lvTweetList.setOnScrollListener(new CustomEndlessScrollListener(5, 25));
        progressLoading = (ProgressBar) fragmentView.findViewById(R.id.pbLoading);
        lvTweetList.setAdapter(arrayAdaptor);

        // Now setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(getActivity())
                // Mark All Children as pullable
                .allChildrenArePullable()
                // Set a OnRefreshListener
                .listener(new OnRefreshListener() {
                    @Override
                    public void onRefreshStarted(View view) {
                        progressLoading.setVisibility(View.VISIBLE);
                        populateTimeLine();
                        pullToRefreshLayout.setRefreshComplete();
                    }
                })
                // Finally commit the setup to our PullToRefreshLayout
                .setup(pullToRefreshLayout);

        lvTweetList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Tweet tweet = (Tweet)parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), TweetMessageActivity.class);
                intent.putExtra("tweet", tweet);
                startActivityForResult(intent, REQUEST_MESSAGE_DETAIL);
                positionSelected = position;
                viewSelected = view;
                return true;
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_MESSAGE_DETAIL) {
            if (resultCode == Activity.RESULT_OK) {
                final Tweet tweet = (Tweet)data.getSerializableExtra("tweet");
                tweetList.set(positionSelected, tweet);
                View view = lvTweetList.getChildAt(positionSelected);
                lvTweetList.getAdapter().getView(positionSelected, view, lvTweetList);
                //TweetArrayAdapter.updateView(this, viewSelected, tweet);
            }
        }
    }

    public void disableProfileClick(boolean value) {
        arrayAdaptor.disableImageViewClick(value);
    }

    public void clearAllTweets() {
        arrayAdaptor.clear();
        Tweet.deleteAllSavedTweet();
    }

    public void addAllTweets(List<Tweet> tweetList) {
        arrayAdaptor.addAll(tweetList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_timeline, container, false);
        init();
        return fragmentView;
    }

    public void addNewTweet(Tweet tweet) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweetList = new ArrayList<Tweet>();
        arrayAdaptor = new TweetArrayAdapter(getActivity(), tweetList, getArguments() != null ? getArguments().getString("user_name"): null, this);
        //arrayAdaptor.addAll(Tweet.getAllSavedTweet());
    }
}
