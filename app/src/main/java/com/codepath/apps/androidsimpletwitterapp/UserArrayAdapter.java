package com.codepath.apps.androidsimpletwitterapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.androidsimpletwitterapp.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by suraj on 27/02/15.
 */
public class UserArrayAdapter extends ArrayAdapter<User> {
    class ViewHolder {
        ImageView profilePic;
        TextView  userName;
        TextView  handleName;
        TextView  description;
        User      user;
    }


    public UserArrayAdapter(Context context, List<User> usersList) {
        super(context, 0, usersList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        User user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_user_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.profilePic = (ImageView)convertView.findViewById(R.id.ivProfilePic);
            viewHolder.userName = (TextView)convertView.findViewById(R.id.tvUserName);
            viewHolder.handleName = (TextView)convertView.findViewById(R.id.tvHandleName);
            viewHolder.description = (TextView)convertView.findViewById(R.id.tvCaption);



            viewHolder.profilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewHolder viewHolder = (ViewHolder)v.getTag();
                    Intent intent = new Intent(UserArrayAdapter.this.getContext(), ProfileActivity.class);
                    intent.putExtra("profile_name", viewHolder.user.getHandleName());
                    intent.putExtra("profile_banner_url", viewHolder.user.getProfileBanner());
                    ((Activity)UserArrayAdapter.this.getContext()).startActivity(intent);
                }
            });

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.profilePic.setTag(viewHolder);
        viewHolder.user = user;
        viewHolder.userName.setText(user.getName());
        viewHolder.handleName.setText("@" + user.getHandleName());
        viewHolder.description.setText(user.getCaption());

        viewHolder.profilePic.setImageResource(0);
        Picasso.with(getContext()).load(user.getProfilePic()).into(viewHolder.profilePic);

        return convertView;
    }
}
