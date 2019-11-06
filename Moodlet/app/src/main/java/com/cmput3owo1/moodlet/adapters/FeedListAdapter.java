package com.cmput3owo1.moodlet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.models.MoodEvent;
import com.cmput3owo1.moodlet.models.MoodEventAssociation;

import java.util.ArrayList;


public class FeedListAdapter extends ArrayAdapter<MoodEventAssociation> {
    private ArrayList<MoodEventAssociation> feed;
    private Context context;

    public FeedListAdapter(Context context, ArrayList<MoodEventAssociation> feed) {
        super(context,0, feed);
        this.feed = feed;
        this.context = context;
    }

    static class ViewHolder {
        TextView usernameTextView;
        TextView emotionalStateTextView;
        TextView dateTextView;
        ImageView emoticonImageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            //TODO this whole thing
            convertView = LayoutInflater.from(context).inflate(R.layout.feed_list_entry, null);
            holder = new ViewHolder();
            holder.usernameTextView = (TextView) convertView.findViewById(R.id.feed_username);
            holder.emotionalStateTextView = (TextView) convertView.findViewById(R.id.feed_emotional_state);
            holder.dateTextView = (TextView) convertView.findViewById(R.id.feed_date);
            holder.emoticonImageView = (ImageView) convertView.findViewById(R.id.feed_emoticon);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        MoodEventAssociation moodEventAssociation = getItem(position);
        MoodEvent moodEvent = moodEventAssociation.getMoodEvent();

        holder.usernameTextView.setText(moodEventAssociation.getUsername());
        holder.emotionalStateTextView.setText(moodEvent.getEmotionalState().name());
        holder.dateTextView.setText(moodEvent.getDate().toString());
        holder.emoticonImageView.setImageBitmap(moodEvent.getPhotograph());

        return convertView;
    }
}
