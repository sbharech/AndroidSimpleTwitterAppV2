package com.codepath.apps.androidsimpletwitterapp;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by suraj on 11/02/15.
 */
public class RecentSearchQuerySuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.codepath.apps.androidsimpletwitterapp.suggestion.authority";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public RecentSearchQuerySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}

