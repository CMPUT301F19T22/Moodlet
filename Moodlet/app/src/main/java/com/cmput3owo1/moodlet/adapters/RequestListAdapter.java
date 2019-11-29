package com.cmput3owo1.moodlet.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.media.Image;
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
import com.cmput3owo1.moodlet.models.User;
import com.cmput3owo1.moodlet.services.IUserServiceProvider;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class RequestListAdapter extends ArrayAdapter<FollowRequest> {
    private ArrayList<FollowRequest> requestsList;
    private Context context;
    private OnAcceptClickListener acceptListener;
    private OnDeclineClickListener declineListener;

    public interface OnAcceptClickListener {
        void OnAcceptClick(FollowRequest requestFrom);
    }

    public interface OnDeclineClickListener {
        void OnDeclineClick(FollowRequest requestFrom);
    }

    public RequestListAdapter(Context context,
                              ArrayList<FollowRequest> requestsList,
                              OnAcceptClickListener acceptListener, OnDeclineClickListener declineListener) {

        super(context, 0, requestsList);
        this.requestsList = requestsList;
        this.acceptListener = acceptListener;
        this.declineListener = declineListener;
    }

    static class ViewHolder {
        TextView usernameTextView;
        ImageButton acceptButton;
        ImageButton declineButton;
    }

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

        final FollowRequest requestFrom = getItem(position);

        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptListener.OnAcceptClick(requestFrom);
            }
        });

        holder.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declineListener.OnDeclineClick(requestFrom);
            }
        });

        return convertView;
    }
}
