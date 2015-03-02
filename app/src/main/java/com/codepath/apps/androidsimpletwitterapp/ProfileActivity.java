package com.codepath.apps.androidsimpletwitterapp;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.androidsimpletwitterapp.R;
import com.codepath.apps.androidsimpletwitterapp.fragments.FavoriteTimeLineFragment;
import com.codepath.apps.androidsimpletwitterapp.fragments.FollowersUserListFragment;
import com.codepath.apps.androidsimpletwitterapp.fragments.FollowingUserListFragment;
import com.codepath.apps.androidsimpletwitterapp.fragments.UserTimeLineFragment;
import com.codepath.apps.androidsimpletwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends ActionBarActivity {
    User user = null;
    UserTimeLineFragment userTimeLineFragment = null;
    FavoriteTimeLineFragment favoriteTimeLineFragment = null;
    FollowingUserListFragment followingListFragment = null;
    FollowersUserListFragment followersListFragment = null;

    String userProfileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00aced")));

        userProfileName = getIntent().getStringExtra("profile_name");
        String bannerPic = getIntent().getStringExtra("profile_banner_url");

        setTitle("@" + userProfileName);
        setContentView(R.layout.activity_profile);

        populateUserInfo(userProfileName, bannerPic);

        if (savedInstanceState == null) {
            userTimeLineFragment = UserTimeLineFragment.getNewInstance(userProfileName);
            favoriteTimeLineFragment = FavoriteTimeLineFragment.getNewInstance(userProfileName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flTimeLine, userTimeLineFragment);
            ft.commit();
        }
    }


    public void onButtonClick(View v) {
        if (v.getId() == R.id.btnFavorites) {
            if (favoriteTimeLineFragment == null)
                favoriteTimeLineFragment = FavoriteTimeLineFragment.getNewInstance(userProfileName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flTimeLine, favoriteTimeLineFragment);
            ft.commit();
        } else if (v.getId() == R.id.btnTweets) {
            if (userTimeLineFragment == null)
                userTimeLineFragment = UserTimeLineFragment.getNewInstance(userProfileName);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flTimeLine, userTimeLineFragment);
            ft.commit();
        } else if (v.getId() == R.id.btnFollowingCount) {
            if (followingListFragment == null)
                followingListFragment = FollowingUserListFragment.getNewInstance(userProfileName);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flTimeLine, followingListFragment);
            ft.commit();
        } else if (v.getId() == R.id.btnFollowerCount) {
            if (followersListFragment == null)
                followersListFragment = FollowersUserListFragment.getNewInstance(userProfileName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flTimeLine, followersListFragment);
            ft.commit();
        }
    }


    private void displayUser() {
        int width = Utility.getDisplayWidth(this);


        ImageView profilePic = (ImageView)findViewById(R.id.ivProfilePic);
        Picasso.with(this).load(user.getProfilePic()).into(profilePic);
        TextView  userName = (TextView)findViewById(R.id.tvUserName);
        userName.setText(user.getName());
        TextView  handleName = (TextView)findViewById(R.id.tvHandleName);
        handleName.setText("@" + user.getHandleName());
        Button followerCount = (Button)findViewById(R.id.btnFollowerCount);
        Resources res = getResources();
        String quantityString = res.getQuantityString(R.plurals.follower_count,
                user.getFollower(), user.getFollower());
        followerCount.setText(Utility.coolStringFormat(user.getFollower()) + " " + quantityString);
        Button followingCount = (Button)findViewById(R.id.btnFollowingCount);
        followerCount.setWidth(width/4);
        followingCount.setWidth(width/4);
        followingCount.setText(Utility.coolStringFormat(user.getFollowing()) + " " + getString(R.string.following));
        ImageView bannerPic = (ImageView)findViewById(R.id.ivTimelinePic);
        Picasso.with(this).load(user.getProfileBanner()).into(bannerPic);
        TextView caption = (TextView)findViewById(R.id.tvCaption);
        if (user.getCaption().length() > 0)
            caption.setText(user.getCaption());

        Button tweetButton = (Button)findViewById(R.id.btnTweets);
        tweetButton.setWidth(width/4);
        tweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(v);
            }
        });

        Button favButton = (Button)findViewById(R.id.btnFavorites);
        tweetButton.setWidth(width/4);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(v);
            }
        });

        followingCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(v);
            }
        });

        followerCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(v);
            }
        });

        /*followingCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, FollowingFollowerActivity.class);
                intent.putExtra("profile_name", user.getHandleName());
                intent.putExtra("type", 0);
                startActivity(intent);
            }
        });


        followerCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, FollowingFollowerActivity.class);
                intent.putExtra("profile_name", user.getHandleName());
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });*/
    }

    private void populateUserInfo(final String userProfileName, final String bannerPic) {
            TwitterApplication.getRestClient().getUserProfile(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.i("user display", response.toString());
                            ProfileActivity.this.user = User.getUser(response);
                            if (ProfileActivity.this.user.getProfileBanner() == null && bannerPic != null)
                                ProfileActivity.this.user.setProfileBanner(bannerPic);
                            displayUser();

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                    Log.i("error", errorResponse.toString());
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

            }, userProfileName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
