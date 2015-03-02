package com.codepath.apps.androidsimpletwitterapp;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.codepath.apps.androidsimpletwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by suraj on 18/02/15.
 */
@Table(name="currentUser")
public class CurrentUser extends Model {
    @Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private static User currentUser = null;

    public static CurrentUser getInstance() {
        CurrentUser c = new CurrentUser();
        c.populateCurrentUser();
        return c;
    }


    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }


    public void populateCurrentUser() {
        if (currentUser != null)
            return;

        currentUser = new Select().from(CurrentUser.class).executeSingle();


        TwitterApplication.getRestClient().getUserProfile(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                User user = User.getUser(response);
                setCurrentUser(user);
                new Delete().from(CurrentUser.class).execute();
                currentUser.save();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

        }, null);
    }
}
