package com.cmput3owo1.moodlet.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.models.User;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

/**
 * This class is the adapter class to a ListView containing {@link User} objects.
 * The purpose is to connect the User data with the ListView and to create the
 * {@link ViewHolder} objects that will display the data. It converts a user
 * object at a position to the view in the list.
 */
public class UserListAdapter extends ArrayAdapter<User> {
    private ArrayList<User> userList;
    private Context context;
    private OnFollowClickListener listener;

    /**
     * Listener interface called when the follow button by the user is selected.
     */
    public interface OnFollowClickListener {
        void onFollowClick(User user);
    }

    /**
     * Constructor that takes the context and the ArrayList of {@link User} objects
     * @param context The app context
     * @param userList The list of User objects to display
     * @param listener The listener to notify on follow button click
     */
    public UserListAdapter(Context context, ArrayList<User> userList, OnFollowClickListener listener) {
        super(context, 0, userList);
        this.userList = userList;
        this.context = context;
        this.listener = listener;
    }

    /**
     * ViewHolder class that holds the views inside of a user in the list
     */
    static class ViewHolder {
        TextView usernameTextView;
        MaterialButton requestButton;
    }

    /**
     * Get a view that displays the data at the specified position in the data set
     * @param position The position of the item within the adapter's data set
     * @param convertView The old view to reuse, if possible
     * @param parent The parent that this view will eventually be attached to
     * @return The view that displays the data
     */
    @Override
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.search_list_entry, null);
            holder = new ViewHolder();
            holder.usernameTextView = convertView.findViewById(R.id.search_username);
            holder.requestButton = convertView.findViewById(R.id.search_request_button);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final User user = getItem(position);
        Resources resources = context.getResources();

        String usernameDisplayText = "@" + user.getUsername();
        holder.usernameTextView.setText(usernameDisplayText);
        if (user.isFollowing()) {
            holder.requestButton.setText(resources.getString(R.string.following));
            holder.requestButton.setEnabled(false);
        } else if (user.isRequested()) {
            holder.requestButton.setText(resources.getString(R.string.requested));
            holder.requestButton.setEnabled(false);
        } else {
            holder.requestButton.setText(resources.getString(R.string.follow));
            holder.requestButton.setEnabled(true);
        }

        holder.requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFollowClick(user);
            }
        });

        return convertView;
    }
}
