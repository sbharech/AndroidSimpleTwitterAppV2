package com.codepath.apps.androidsimpletwitterapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.androidsimpletwitterapp.fragments.HomeTimeLineFragment;
import com.codepath.apps.androidsimpletwitterapp.fragments.MentionsTimeLineFragment;
import com.codepath.apps.androidsimpletwitterapp.fragments.TimeLineFragment;
import com.codepath.apps.androidsimpletwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class HomeTimelineActivity extends ActionBarActivity implements TweetComposerFragment.TweetComposerFragmentResultListener, TimeLineFragment.ValueChangeListener {
    public static final int REQUEST_NEW_TWEET = 0;
    private MentionsTimeLineFragment mentionstimelineFragment;
    private HomeTimeLineFragment hometimelineFragment;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_timeline);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00aced")));
        setTitle("");

        CurrentUser.getInstance().populateCurrentUser();

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        FragmentPagerAdapter adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);
        tabStrip.setIndicatorColor(Color.parseColor("#00aced"));
        handleIntent(getIntent());
        //tabStrip.setIndicatorColor(44269);

    }

    @Override
    public void setResult(Intent intent) {
        final Tweet tweet = (Tweet)intent.getSerializableExtra("tweet");
        Log.i("New Tweet", "Created a new one with id " + tweet.getTweetId());
        if (tweet.getTweetId() > 0) {
            TwitterApplication.getRestClient().getMessage(new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Tweet newTweet = Tweet.getTweet(response);
                    //tweetList.add(0, newTweet);
                    //arrayAdaptor.notifyDataSetInvalidated();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.i("Error....", "Error");
                }

            },tweet);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void saveRecentSearchQuery(String query) {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                RecentSearchQuerySuggestionProvider.AUTHORITY, RecentSearchQuerySuggestionProvider.MODE);
        suggestions.saveRecentQuery(query, null);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String currentQueryString = intent.getStringExtra(SearchManager.QUERY);
            saveRecentSearchQuery(currentQueryString);
            Intent intentNew = new Intent(this, SearchActivity.class);
            try {
                currentQueryString = URLEncoder.encode(currentQueryString, "UTF-8");
                intentNew.putExtra("query", currentQueryString);
                startActivity(intentNew);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_NEW_TWEET) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                final Tweet tweet = (Tweet)data.getSerializableExtra("tweet");
                Log.i("New Tweet", "Created a new one with id " + tweet.getTweetId());
                if (tweet.getTweetId() > 0) {
                    TwitterApplication.getRestClient().getMessage(new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Tweet newTweet = Tweet.getTweet(response);
                            if (HomeTimelineActivity.this.mentionstimelineFragment != null)
                                HomeTimelineActivity.this.mentionstimelineFragment.addNewTweet(newTweet);
                            if (HomeTimelineActivity.this.hometimelineFragment != null)
                                HomeTimelineActivity.this.hometimelineFragment.addNewTweet(newTweet);
                            //tweetList.add(0, newTweet);
                            //arrayAdaptor.notifyDataSetInvalidated();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.i("Error....", "Error");
                        }


                    },tweet);
                }
            }
        }
    }

    @Override
    public void notifyValueChange(Tweet tweet, Fragment fragment) {

        if (fragment == hometimelineFragment)
            mentionstimelineFragment.updateTweetInfo(tweet);
        else
            hometimelineFragment.updateTweetInfo(tweet);
    }

    public  class MyPagerAdapter extends FragmentPagerAdapter {
        private String[] pages = {"Home", "Mentions"};

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return pages.length;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return (HomeTimelineActivity.this.hometimelineFragment = new HomeTimeLineFragment());
                case 1:
                    return (HomeTimelineActivity.this.mentionstimelineFragment = new MentionsTimeLineFragment());
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return pages[position];
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_timeline, menu);

        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) searchMenuItem.getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (searchMenuItem != null) {
                    searchMenuItem.collapseActionView();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {

            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                if (searchMenuItem != null) {
                    searchMenuItem.collapseActionView();
                }
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_compose) {
            Intent i = new Intent(this, ComposeActivity.class);
            startActivityForResult(i, REQUEST_NEW_TWEET);
            return true;
        } else if (id == R.id.action_profile) {
            Intent i = new Intent(this, ProfileActivity.class);
            if (CurrentUser.getInstance().getCurrentUser() != null)
               i.putExtra("profile_name", CurrentUser.getInstance().getCurrentUser().getHandleName());
            startActivity(i);
            return true;
        }



        return super.onOptionsItemSelected(item);
    }
}
