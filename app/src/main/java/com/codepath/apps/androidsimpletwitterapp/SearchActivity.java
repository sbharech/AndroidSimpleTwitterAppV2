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
import com.codepath.apps.androidsimpletwitterapp.fragments.SearchQueryTimeLineFragment;
import com.codepath.apps.androidsimpletwitterapp.fragments.UserTimeLineFragment;

public class SearchActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00aced")));




        String query = getIntent().getStringExtra("query");

        setTitle(query);
        setContentView(R.layout.activity_search);

        if (savedInstanceState == null) {
            SearchQueryTimeLineFragment searchQueryTimeLineFragment = SearchQueryTimeLineFragment.getNewInstance(query);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flSearchResult, searchQueryTimeLineFragment);
            ft.commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
