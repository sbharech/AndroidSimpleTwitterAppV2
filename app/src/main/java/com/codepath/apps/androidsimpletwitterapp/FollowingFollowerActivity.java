package com.codepath.apps.androidsimpletwitterapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.androidsimpletwitterapp.R;
import com.codepath.apps.androidsimpletwitterapp.fragments.FavoriteTimeLineFragment;
import com.codepath.apps.androidsimpletwitterapp.fragments.FollowersUserListFragment;
import com.codepath.apps.androidsimpletwitterapp.fragments.FollowingUserListFragment;
import com.codepath.apps.androidsimpletwitterapp.fragments.UserListFragment;
import com.codepath.apps.androidsimpletwitterapp.fragments.UserTimeLineFragment;

public class FollowingFollowerActivity extends ActionBarActivity {
    UserListFragment userListFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00aced")));
        setTitle("");

        String userProfileName = getIntent().getStringExtra("profile_name");
        int followingOrFollower = getIntent().getIntExtra("type", 0);

        setContentView(R.layout.activity_following_follower);

        if (savedInstanceState == null) {
            if (followingOrFollower == 0)
                userListFragment = FollowingUserListFragment.getNewInstance(userProfileName);
            else
                userListFragment = FollowersUserListFragment.getNewInstance(userProfileName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flTimeLine, userListFragment);
            ft.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_following_follower, menu);
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
