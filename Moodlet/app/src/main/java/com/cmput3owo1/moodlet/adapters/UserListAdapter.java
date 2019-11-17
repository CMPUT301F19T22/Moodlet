package com.cmput3owo1.moodlet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.models.User;

import java.util.ArrayList;

public class UserListAdapter extends ArrayAdapter<User> {
    private ArrayList<User> userList;
    private Context context;

    public UserListAdapter(Context context, ArrayList<User> userList) {
        super(context,0, userList);
        this.userList = userList;
        this.context = context;
    }

    static class ViewHolder {
        TextView usernameTextView;
        Button requestButton;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.search_list_entry, null);
            holder = new ViewHolder();
            holder.usernameTextView = (TextView) convertView.findViewById(R.id.search_username);
            holder.requestButton = (Button) convertView.findViewById(R.id.search_request_button);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        User user = getItem(position);
        holder.usernameTextView.setText(user.getUsername());
        // TODO do something with the button?

        return convertView;
    }
}
