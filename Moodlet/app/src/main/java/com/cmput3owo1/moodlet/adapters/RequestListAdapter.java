package com.cmput3owo1.moodlet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.models.FollowRequest;

import java.util.ArrayList;

/**
 * This class is the adapter class to a ListView containing {@link FollowRequest} objects.
 * The purpose is to connect the FollowRequest data with the ListView and to create the
 * {@link ViewHolder} objects that will display the data. It converts a FollowRequest
 * object at a position to the view in the list.
 */
public class RequestListAdapter extends ArrayAdapter<FollowRequest> {
    private ArrayList<FollowRequest> requestsList;
    private Context context;
    private OnRequestClickListener listener;

    /**
     * Listener interface to get the mood event that was selected to be accepted/declined
     */
    public interface OnRequestClickListener {
        void OnAcceptClick(FollowRequest request);
        void OnDeclineClick(FollowRequest request);
    }

    /**
     * Constructor that takes the context and the ArrayList of {@link FollowRequest} objects
     * @param context The app context
     * @param requestsList The list of FollowRequest objects to display
     * @param listener The listener when a request is selected to accept/decline
     */
    public RequestListAdapter(Context context, ArrayList<FollowRequest> requestsList,
                              OnRequestClickListener listener) {
        super(context, 0, requestsList);
        this.requestsList = requestsList;
        this.context = context;
        this.listener = listener;
    }

    /**
     * ViewHolder class that holds the views inside of a follow request in the list
     */
    static class ViewHolder {
        TextView usernameTextView;
        ImageButton acceptButton;
        ImageButton declineButton;
    }

    /**
     * Get a view that displays the data at the specified position in the data set
     * @param position The position of the item within the adapter's data set
     * @param convertView The old view to reuse, if possible
     * @param parent The parent that this view will eventually be attached to
     * @return The view that displays the data
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RequestListAdapter.ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.request_list_row, null);
            holder = new ViewHolder();
            holder.usernameTextView = convertView.findViewById(R.id.request_username);
            holder.acceptButton = convertView.findViewById(R.id.request_accept);
            holder.declineButton = convertView.findViewById(R.id.request_deny);
            convertView.setTag(holder);
        }
        else {
            holder = (RequestListAdapter.ViewHolder) convertView.getTag();
        }

        final FollowRequest request = getItem(position);
        String usernameDisplayText = "@" + request.getRequestFrom();
        holder.usernameTextView.setText(usernameDisplayText);

        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnAcceptClick(request);
            }
        });

        holder.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnDeclineClick(request);
            }
        });

        return convertView;
    }
}
