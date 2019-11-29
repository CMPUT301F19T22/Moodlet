package com.cmput3owo1.moodlet.fragments;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.activities.LoginActivity;
import com.cmput3owo1.moodlet.models.EmotionalState;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.cmput3owo1.moodlet.fragments.MoodHistoryFragmentTest.atPosition;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FilterTest {

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule(LoginActivity.class, false, true);

    // Start with MainActivity (JUnit Rules: https://developer.android.com/training/testing/junit-rules)
    private String testEmail = "test@ua.ca";
    private String testPassword = "123123";

    private void loginWithTestAccount() throws InterruptedException {
        onView(withId(R.id.edit_text_email)).perform(typeText(testEmail));
        pressBack();
        onView(withId(R.id.edit_text_password)).perform(typeText(testPassword));
        pressBack();
        onView(withId(R.id.btn_login)).perform(click());

        //Inelegant way to wait for network calls. Fix if there's time
        Thread.sleep(5000);
    }
    
    private void addMood(EmotionalState chosenEmotion) throws InterruptedException {
        // Click on the floating action button to go to add mood page
        onView(withId(R.id.add_mood_fab)).perform(click());

        // Click on mood drop down and select a mood
        onView(withId(R.id.moodSelected)).perform(click());

        // Select jealous from drop down
        onData(Matchers.allOf(is(instanceOf(EmotionalState.class)), is(chosenEmotion))).perform(click());

        // add mood event
        onView(withId(R.id.confirmAdd)).perform(click());

        // Sleep for async call to Firebase
        Thread.sleep(500);
    }


    @Test
    public void testFilterActivity() throws InterruptedException {
        // Navigate to History Fragment
        onView(withId(R.id.navigation_mood_history)).perform(click());

        // Click Filter menu item
        onView(withId(R.id.filter)).perform(click());

        // Check if on filter activity
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Filter"))));
    }

    @Test
    public void testApplyingFilter() throws InterruptedException {
        loginWithTestAccount();

        // Navigate to History Fragment
        onView(withId(R.id.navigation_mood_history)).perform(click());

        // filter all moods except for jealous
        onView(withId(R.id.filter)).perform(click());
        onView(withId(R.id.filter_happy)).perform(click());


        // apply filter changes
        onView(withId(R.id.apply_filter_button)).perform(click());
        Thread.sleep(3000);

        // Check if only nonfiltered item is HAPPY
        onView(withId(R.id.mood_event_rv)).check(matches(atPosition(0, hasDescendant(withText("Happy")))));
        onView(withId(R.id.mood_event_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.moodDisplay)).check(matches(withText("Happy")));

    }
}
