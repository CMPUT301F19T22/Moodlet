package com.cmput3owo1.moodlet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.services.IUserServiceProvider;
import com.cmput3owo1.moodlet.services.UserService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

/**
 * The main activity. Sets up the navbar and the navigation to the MoodHistoryFragment, the
 * FeedFragment, the SearchFragment, and the MapFragment.
 */
public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private IUserServiceProvider userService;
    /**
     * Called when the activity is starting. Sets up the navigation.
     * @param savedInstanceState Used to restore an activity's previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userService = new UserService();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//      Manually replace navigation icon with custom icon.
        toolbar.setNavigationIcon(R.drawable.ic_group_add_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                 TODO: Create intent to go to the follow requests fragment.
                Toast.makeText(MainActivity.this, "Follow requests", Toast.LENGTH_SHORT).show();
                openFollowRequestActivity();
            }
        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_mood_history, R.id.navigation_feed, R.id.navigation_search,
                R.id.navigation_map)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    private void openFollowRequestActivity(){
        Intent intent = new Intent(this, FollowRequestActivity.class);
        startActivity(intent);
    }


    /** Initialize the contents of the Activity's standard options menu.
     * @param menu The options menu in which you place your items.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.general_fragment_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * A hook that is called whenever an item in the options menu is selected.
     * This method can be used for adding functionality for specific selected menu items.
     * @param item The menu item that was selected
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.logout:
                userService.logoutUser();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
