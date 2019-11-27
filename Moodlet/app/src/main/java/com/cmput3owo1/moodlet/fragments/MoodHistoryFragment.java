package com.cmput3owo1.moodlet.fragments;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.activities.MoodEditorActivity;
import com.cmput3owo1.moodlet.adapters.MoodEventAdapter;
import com.cmput3owo1.moodlet.models.MoodEvent;
import com.cmput3owo1.moodlet.services.IMoodEventServiceProvider;
import com.cmput3owo1.moodlet.services.MoodEventService;
import com.cmput3owo1.moodlet.services.UserService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A fragment that hold the list of user's mood events while displaying the emotion, date,
 * and time sorted in reverse chronological order of each mood event.
 */
public class MoodHistoryFragment extends Fragment
        implements MoodEventAdapter.OnItemClickListener, IMoodEventServiceProvider.OnMoodHistoryUpdateListener, IMoodEventServiceProvider.OnMoodDeleteListener{

    private RecyclerView recyclerView;
    private MoodEventAdapter recyclerAdapter;
    private ArrayList<MoodEvent> moodEventList;
    private IMoodEventServiceProvider moodEventService;
    private FloatingActionButton addMood;

    /**
     * Called when the fragment is starting.
     * @param savedInstanceState Used to restore a fragment's previous state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * This function is called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mood_history, container, false);

        recyclerView = view.findViewById(R.id.mood_event_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        moodEventList = new ArrayList<>();

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        recyclerAdapter = new MoodEventAdapter(moodEventList, this);
        recyclerView.setAdapter(recyclerAdapter);

        moodEventService = new MoodEventService();
        moodEventService.getMoodHistoryUpdates(this);

        addMood = view.findViewById(R.id.add_mood_fab);
        addMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MoodEditorActivity.class);
                intent.putExtra("add",true);
                startActivity(intent);
            }
        });

        return view;
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     * @param menu The options menu in which you place your items.
     * @param inflater The MenuInflater object that can be used to inflate any itmes in the menu
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.mood_history_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    /*
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.filter:
                TODO: Show filter options when pressed and apply filter
        }

        return super.onOptionsItemSelected(item);
    }
     */

    /**
     * Called when the user is clicking on a mood event to edit
     * @param pos Gets position of the item clicked in the RecyclerView
     */
    @Override
    public void onItemClick(int pos) {
        // Implement for editing a mood event
        MoodEvent selected = moodEventList.get(pos);
        Intent intent = new Intent(getActivity(), MoodEditorActivity.class);
        intent.putExtra("MoodEvent",selected);
        intent.putExtra("date",selected.getDate());
        intent.putExtra("view",true);
        startActivity(intent);
    }

    /**
     * Gets called when there is an update to the user's Mood Event History
     * @param newHistory The updated Mood Event History
     */
    @Override
    public void onMoodHistoryUpdate(ArrayList<MoodEvent> newHistory) {
        moodEventList.clear();
        moodEventList.addAll(newHistory);
        recyclerAdapter.notifyDataSetChanged();
    }

    /**
     * Utility class to add swipe support to recycler view for deleting a mood event
     */
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        /**
         * Called when ItemTouchHelper wants to move the dragged item from its old position to the new position
         * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to
         * @param viewHolder The ViewHolder which is being dragged by the user
         * @param target  The ViewHolder over which the currently active item is being dragged
         * @return If this method returns true, ItemTouchHelper assumes viewHolder has been moved to the adapter position of target ViewHolder, else, it hasn't been moved
         */
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        /**
         * Called when a ViewHolder is swiped by the user
         * @param viewHolder The ViewHolder which has been swiped by the user
         * @param direction The direction to which the ViewHolder is swiped
         */
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//            MoodEvent deletedMood = moodEventList.remove(viewHolder.getAdapterPosition());
//            moodEventService.deleteMoodEvent(getActivity(), deletedMood, MoodHistoryFragment.this);
//            recyclerAdapter.notifyDataSetChanged();
            moodEventService.deleteMoodEvent(moodEventList.get(viewHolder.getAdapterPosition()), MoodHistoryFragment.this);
        }

        /**
         * Called by ItemTouchHelper on RecyclerView's onDraw callback
         * @param c The canvas which RecyclerView is drawing its children
         * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to
         * @param viewHolder The ViewHolder which is being interacted by the User (or it was interacted and simply animating to its original position)
         * @param dX Amount of horizontal displacement caused by user's action
         * @param dY Amount of vertical displacement caused by user's action
         * @param actionState The type of interaction on the View
         * @param isCurrentlyActive  True if view is currently being controlled by the user or false it is animating back to its original state
         */
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            View itemView = viewHolder.itemView;

            final ColorDrawable background = new ColorDrawable(Color.RED);
            background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(),   itemView.getRight(), itemView.getBottom());
            background.draw(c);

        }
    };

    /**
     * Callback function that is triggered upon successfully deleting a MoodEvent
     */
    @Override
    public void onMoodDeleteSuccess() {
        Toast.makeText(getActivity(), R.string.delete_success, Toast.LENGTH_SHORT).show();
    }

    /**
     * Callback function that is triggered upon failing to delete a MoodEvent
     */
    @Override
    public void onMoodDeleteFailure() {
        Toast.makeText(getActivity(), R.string.delete_failed, Toast.LENGTH_SHORT).show();
    }
}


