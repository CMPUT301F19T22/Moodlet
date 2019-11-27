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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * This class is the adapter class to a ListView containing {@link MoodEventAssociation} objects.
 * The purpose is to connect the MoodEventAssociation data with the ListView and to create the
 * {@link ViewHolder} objects that will display the data. It converts a MoodEventAssociation
 * object at a position to the view in the list.
 */
public class FeedListAdapter extends ArrayAdapter<MoodEventAssociation> {
    private ArrayList<MoodEventAssociation> feed;
    private Context context;
    private SimpleDateFormat simpleDateFormat;

    /**
     * Constructor that takes the context and the ArrayList of {@link MoodEventAssociation} objects
     * @param context The app context
     * @param feed The list of MoodEventAssociation objects
     */
    public FeedListAdapter(Context context, ArrayList<MoodEventAssociation> feed) {
        super(context,0, feed);
        this.feed = feed;
        this.context = context;
        this.simpleDateFormat = new SimpleDateFormat("MMMM d, yyyy - h:mm a", Locale.US);
    }

    /**
     * ViewHolder class that holds the views inside of a feed in the list
     */
    static class ViewHolder {
        TextView usernameTextView;
        TextView emotionalStateTextView;
        TextView dateTextView;
        ImageView emoticonImageView;
    }

    /**
     * Get a view that displays the data at the specified position in the data set
     * @param position The position of the item within the adapter's data set
     * @param convertView The old view to reuse, if possible
     * @param parent The parent that this view will eventually be attached to
     * @return The view that displays the data
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.feed_list_entry, null);
            holder = new ViewHolder();
            holder.usernameTextView = (TextView) convertView.findViewById(R.id.feed_username);
            holder.emotionalStateTextView = (TextView) convertView.findViewById(R.id.feed_mood);
            holder.dateTextView = (TextView) convertView.findViewById(R.id.feed_date);
            holder.emoticonImageView = (ImageView) convertView.findViewById(R.id.feed_mood_emoji);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        MoodEventAssociation moodEventAssociation = getItem(position);
        MoodEvent moodEvent = moodEventAssociation.getMoodEvent();
        String dateString = simpleDateFormat.format(moodEvent.getDate());
        String moodDisplayText =  moodEvent.getEmotionalState().getDisplayName();
        String usernameDisplayText = "@" + moodEventAssociation.getUsername();

        holder.usernameTextView.setText(usernameDisplayText);
        holder.emotionalStateTextView.setText(moodDisplayText);
        holder.dateTextView.setText(dateString);

        switch(moodEvent.getEmotionalState()) {
            case SAD:
                holder.emoticonImageView.setImageResource(R.drawable.ic_mood_sad);
                break;
            case ANGRY:
                holder.emoticonImageView.setImageResource(R.drawable.ic_mood_angry);
                break;
            case CONFUSED:
                holder.emoticonImageView.setImageResource(R.drawable.ic_mood_confused);
                break;
            case EXCITED:
                holder.emoticonImageView.setImageResource(R.drawable.ic_mood_excited);
                break;
            case HAPPY:
                holder.emoticonImageView.setImageResource(R.drawable.ic_mood_happy);
                break;
            case JEALOUS:
                holder.emoticonImageView.setImageResource(R.drawable.ic_mood_jealous);
                break;
            case SCARED:
                holder.emoticonImageView.setImageResource(R.drawable.ic_mood_scared);
                break;
        }


        return convertView;
    }
}
