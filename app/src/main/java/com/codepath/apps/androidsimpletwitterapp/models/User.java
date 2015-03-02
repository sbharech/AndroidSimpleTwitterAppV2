package com.codepath.apps.androidsimpletwitterapp.models;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.codepath.apps.androidsimpletwitterapp.TwitterApplication;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suraj on 16/02/15.
 */
@Table(name = "users")
public class User extends Model implements Serializable {
    @Column(name = "userId", unique = true, onUniqueConflict = Column.ConflictAction.IGNORE)
    private long userId;
    @Column(name = "name")
    private String name;
    @Column(name = "handleName")
    private String handleName;
    @Column(name = "profilePic")
    private String profilePic;
    @Column(name="follower")
    private int follower;
    @Column(name="following")
    private int following;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Column(name="caption")
    private String caption = "";

    public String getProfileBanner() {
        return profileBanner;
    }

    public void setProfileBanner(String profileBanner) {
        this.profileBanner = profileBanner;
    }
    @Column(name="profile_banner")
    private String profileBanner = null;


    public User() {
        super();
    }

    public String getHandleName() {
        return handleName;
    }

    public static ArrayList<User> getUsersList(JSONArray array) {
        ArrayList<User> usersList = new ArrayList<User>();
        for (int i = 0; i < array.length(); ++i) {
            try {
                usersList.add(User.getUser(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return usersList;
    }

    private static class UserBannerJsonHandler extends JsonHttpResponseHandler {
        User user = null;
        UserBannerJsonHandler(User user) {
            this.user = user;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                Log.i("banner ", response.getJSONObject("mobile").getString("url"));
                user.setProfileBanner(response.getJSONObject("mobile").getString("url"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public static User getUser(JSONObject object) {

        try {
            User user = new Select().from(User.class).where("userId = ?", object.getLong("id")).executeSingle();
            if (user == null) {
                user = new User();
                user.userId = object.getLong("id");
                user.name = object.getString("name");
                user.profilePic = object.getString("profile_image_url");
                user.handleName = object.getString("screen_name");
                user.follower = object.getInt("followers_count");
                user.following = object.getInt("friends_count");
                user.caption = object.getString("description");
                try {
                    user.profileBanner = object.getString("profile_banner_url");
                } catch (JSONException e) {

                }


                user.save();
            }

            //if (user.profileBanner == null) {
            //    TwitterApplication.getRestClient().getProfileBanner(new UserBannerJsonHandler(user), user.handleName);
            //}


            return user;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Tweet> items() {
        return getMany(Tweet.class, "User");
    }

    public String getName() {
        return name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public static User getUser(long userId) {
        return new Select()
                .from(User.class)
                .where("id = ?", userId)
                .orderBy("id DESC")
                .executeSingle();
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }
}
