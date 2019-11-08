package com.cmput3owo1.moodlet.fragments;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * This tests to see if the Google Map is loaded when navigating to the Map section
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MapFragmentTest {

    // Start with MainActivity (JUnit Rules: https://developer.android.com/training/testing/junit-rules)
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule(MainActivity.class, false, true);

    @Test
    public void testMapFragmentDisplayed() {
        // Navigate to the MapFragment
        onView(withId(R.id.navigation_map)).perform(click());

        // Get the action bar text and check that it is displayed
        onView(allOf(withParent(withId(R.id.action_bar)), withText("Map"))).check(matches(isDisplayed()));

        // Check that the MapView is displayed
        onView(withId(R.id.mapView)).check(matches(isDisplayed()));
    }
}
