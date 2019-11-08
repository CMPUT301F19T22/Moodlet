package com.cmput3owo1.moodlet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.models.MoodEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MoodEventAdapter extends RecyclerView.Adapter<MoodEventAdapter.ViewHolder> {

    private ArrayList<MoodEvent> moodEventList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    public MoodEventAdapter(ArrayList<MoodEvent> moodEventList, OnItemClickListener listener) {
        this.moodEventList = moodEventList;
        this.listener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvMood, tvDateTime;
//        TextView tvTimeDiff;

        ViewHolder(View itemView) {
            super(itemView);
            tvMood = itemView.findViewById(R.id.mood_textview);
            tvDateTime = itemView.findViewById(R.id.date_time_textview);
//            tvTimeDiff = itemView.findViewById(R.id.time_difference);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            listener.onItemClick(position);
        }
    }

    @NonNull
    @Override
    public MoodEventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.moodhistory_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MoodEvent mood = moodEventList.get(position);


        // set TextViews in RecyclerView
        holder.tvMood.setText(mood.getEmotionalState().name());
        holder.tvDateTime.setText(new SimpleDateFormat("MMMM dd, yyyy - HH:mm").format(mood.getDate()));
//        holder.tvTimeDiff.setText("1s");

    }

    @Override
    public int getItemCount() { return moodEventList.size(); }
}
