package com.cmput3owo1.moodlet.fragments;


import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.activities.LoginActivity;
import com.cmput3owo1.moodlet.models.EmotionalState;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MoodHistoryFragmentTest {

    public static Matcher<View> atPosition(final int position, final Matcher<View> itemMatcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    // Start with MainActivity (JUnit Rules: https://developer.android.com/training/testing/junit-rules)
    private String testEmail = "test@test.com";
    private String testPassword = "Password123!";

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule(LoginActivity.class, false, true);

    private void loginWithTestAccount() throws InterruptedException {
        onView(withId(R.id.edit_text_email)).perform(typeText(testEmail));
        pressBack();
        onView(withId(R.id.edit_text_password)).perform(typeText(testPassword));
        pressBack();
        onView(withId(R.id.btn_login)).perform(click());

        //Inelegant way to wait for network calls. Fix if there's time
        Thread.sleep(5000);
    }
    

    @Test
    public void testAddMoodEvent() throws InterruptedException {
        loginWithTestAccount();

        // Navigate to the History Fragment
        onView(withId(R.id.navigation_mood_history)).perform(click());

        // Click on the floating action button to go to add mood page
        onView(withId(R.id.add_mood_fab)).perform(click());

        // Click on mood drop down and select a mood
        onView(withId(R.id.moodSelected)).perform(click());

        // Select confused from drop down
//        onView(allOf(withId(R.id.standard_spinner_format), withText("Confused"))).perform(click());
        onData(allOf(is(instanceOf(EmotionalState.class)), is(EmotionalState.CONFUSED))).perform(click());

        // add mood event
        onView(withId(R.id.confirmAdd)).perform(click());

        Thread.sleep(3000);
        onView(withId(R.id.mood_event_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.moodDisplay)).check(matches(withText("Confused")));
    }

    @Test
    public void testAddMoodEventCurrLocation() throws InterruptedException {
        loginWithTestAccount();

        // Navigate to the History Fragment
        onView(withId(R.id.navigation_mood_history)).perform(click());

<<<<<<< HEAD
        onView(withId(R.id.mood_event_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.editToggle)).perform((click()));
=======
        // Click on the floating action button to go to add mood page
        onView(withId(R.id.add_mood_fab)).perform(click());
>>>>>>> 2dbdae3bfce3bc1774924afa5e99801e7486e089

        // Click on mood drop down and select a mood
        onView(withId(R.id.moodSelected)).perform(click());

<<<<<<< HEAD
        onView(withId(R.id.confirmEdit)).perform(click());
=======
        // Select confused from drop down
        onData(allOf(is(instanceOf(EmotionalState.class)), is(EmotionalState.CONFUSED))).perform(click());
>>>>>>> 2dbdae3bfce3bc1774924afa5e99801e7486e089

        onView(withId(R.id.currentLocationCheckbox)).perform(click());

        // add mood event
        onView(withId(R.id.confirmAdd)).perform(click());

        Thread.sleep(3000);
        onView(withId(R.id.mood_event_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.locationDisplay)).check(matches(not(withText("None"))));
    }

    @Test
    public void testDeleteMood() throws InterruptedException {
        loginWithTestAccount();

        // Navigate to the History Fragment
        onView(withId(R.id.navigation_mood_history)).perform(click());

        // Click on the floating action button to go to add mood page
        onView(withId(R.id.add_mood_fab)).perform(click());

        // Click on mood drop down and select a mood
        onView(withId(R.id.moodSelected)).perform(click());

        // Select jealous from drop down
        onData(allOf(is(instanceOf(EmotionalState.class)), is(EmotionalState.JEALOUS))).perform(click());

        // add mood event
        onView(withId(R.id.confirmAdd)).perform(click());

        // Sleep for async call to Firebase
        Thread.sleep(3000);

        // Swipe left to delete first item
        onView(withId(R.id.mood_event_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));

        // Sleep for async call to Firebase
        Thread.sleep(3000);

        onView(withId(R.id.mood_event_rv)).check(matches(not(atPosition(0, hasDescendant(withText("JEALOUS"))))));
    }


}
