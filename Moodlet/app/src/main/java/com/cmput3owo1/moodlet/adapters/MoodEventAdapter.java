package com.cmput3owo1.moodlet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.models.MoodEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * This class is the adapter class to a RecyclerView containing {@link MoodEventAdapter.ViewHolder} objects
 * The purpose is to create the {@link ViewHolder} objects that will display the data of user's mood events by
 * converting it to a position in the view of the RecyclerView
 */
public class MoodEventAdapter extends RecyclerView.Adapter<MoodEventAdapter.ViewHolder> {

    private ArrayList<MoodEvent> moodEventList;
    private OnItemClickListener listener;

    /**
     * Listener interface to get the position of a mood event upon a click on the RecyclerView
     */
    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    /**
     * Constructor that takes ArrayList of {@link MoodEvent} and the item click listener
     * @param moodEventList
     * @param listener
     */
    public MoodEventAdapter(ArrayList<MoodEvent> moodEventList, OnItemClickListener listener) {
        this.moodEventList = moodEventList;
        this.listener = listener;
    }


    /**
     * ViewHolder that holds the views of the feed of a user's mood event history list
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvMood, tvDateTime;
//        TextView tvTimeDiff;

        ViewHolder(View itemView) {
            super(itemView);
            tvMood = itemView.findViewById(R.id.mood_textview);
            tvDateTime = itemView.findViewById(R.id.date_time_textview);
//            tvTimeDiff = itemView.findViewById(R.id.time_difference);
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
        holder.tvMood.setText(mood.getEmotionalState().name());
        holder.tvDateTime.setText(new SimpleDateFormat("MMMM dd, yyyy - HH:mm").format(mood.getDate()));
//        holder.tvTimeDiff.setText("1s");

    }

    /**
     * Return size of mood event list
     * @return size of mood event list
     */
    @Override
    public int getItemCount() { return moodEventList.size(); }
}
