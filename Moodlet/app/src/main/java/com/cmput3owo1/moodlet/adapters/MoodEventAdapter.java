package com.cmput3owo1.moodlet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.models.MoodEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.cmput3owo1.moodlet.utils.Utils.getMoodEmoticon;
import static com.cmput3owo1.moodlet.utils.Utils.getTimeDifference;

/**
 * This class is the adapter class to a RecyclerView containing {@link MoodEventAdapter.ViewHolder} objects
 * The purpose is to create the {@link ViewHolder} objects that will display the data of user's mood events by
 * converting it to a position in the view of the RecyclerView
 */
public class MoodEventAdapter extends RecyclerView.Adapter<MoodEventAdapter.ViewHolder> {

    private ArrayList<MoodEvent> moodEventList;
    private OnItemClickListener listener;
    private SimpleDateFormat simpleDateFormat;

    /**
     * Listener interface to get the position of a mood event upon a click on the RecyclerView
     */
    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    /**
     * Constructor that takes ArrayList of {@link MoodEvent} and the item click listener
     * @param moodEventList The list of the MoodEvents to display
     * @param listener The listener to call on item clicks
     */
    public MoodEventAdapter(ArrayList<MoodEvent> moodEventList, OnItemClickListener listener) {
        this.moodEventList = moodEventList;
        this.listener = listener;
        this.simpleDateFormat = new SimpleDateFormat("MMMM d, yyyy - h:mm a", Locale.US);
    }

    /**
     * ViewHolder that holds the views of the feed of a user's mood event history list
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvMood, tvDateTime;
        ImageView tvIcon;
        TextView tvTimeDiff;

        /**
         * Constructor for the view holder that takes the view of the item
         * @param itemView The view of the item
         */
        ViewHolder(View itemView) {
            super(itemView);
            tvMood = itemView.findViewById(R.id.mood_textview);
            tvDateTime = itemView.findViewById(R.id.date_time_textview);
            tvIcon = itemView.findViewById(R.id.mood_emoji);
            tvTimeDiff = itemView.findViewById(R.id.time_difference);
            itemView.setOnClickListener(this);
        }

        /**
         * Get adapter position when it is clicked on
         * @param view The view that was clicked
         */
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            listener.onItemClick(position);
        }
    }

    /**
     * Called when RecyclerView should be constructed with a new View that can be RecyclerView.ViewHolder
     * of the give type to represent an item
     * @param parent The view group into which the new View will be added after it is bound to an adapter position
     * @param viewType The view type of the new View
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public MoodEventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.moodhistory_row, parent, false);
        return new ViewHolder(itemView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method will update the contents
     * of itemView to reflect the item at the given position
     * @param holder The ViewHolder which should be updated to represent the contents of the item given position in the data set
     * @param position The position of the item within the adapter's data set
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MoodEvent mood = moodEventList.get(position);

        // set TextViews in RecyclerView
        holder.tvMood.setText(mood.getEmotionalState().getDisplayName());
        holder.tvDateTime.setText(simpleDateFormat.format(mood.getDate()));
        holder.tvIcon.setImageResource(getMoodEmoticon(mood.getEmotionalState()));
        holder.tvTimeDiff.setText(getTimeDifference(mood.getDate()));
    }

    /**
     * Return size of mood event list
     * @return size of mood event list
     */
    @Override
    public int getItemCount() { return moodEventList.size(); }
}
