package com.cmput3owo1.moodlet.fragments;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.activities.LoginActivity;
import com.cmput3owo1.moodlet.models.EmotionalState;
import com.cmput3owo1.moodlet.models.SocialSituation;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class ViewMoodFragmentTest {

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule(LoginActivity.class, false, true);

    // Start with MainActivity (JUnit Rules: https://developer.android.com/training/testing/junit-rules)
    private String testEmail = "test@test.com";
    private String testPassword = "Password123!";

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
    public void testEditingMoodEvent() throws InterruptedException {
        loginWithTestAccount();

        // Navigate to the History Fragment
        onView(withId(R.id.navigation_mood_history)).perform(click());

        // Click the topmost Mood Event
        onView(withId(R.id.mood_event_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Click the edit icon to move to edit mode
        onView(withId(R.id.editToggle)).perform((click()));

        // Clear Location field if not already empty by unchecking this box
        onView(withId(R.id.usePreviousLocationCheckbox)).perform(click());

        // Select "Happy" from the dropdown selector
        onView(withId(R.id.moodSelected)).perform(click());
        onData(allOf(is(instanceOf(EmotionalState.class)), is(EmotionalState.HAPPY))).perform(click());

        // Select "With several people" from the dropdown selector
        onView(withId(R.id.socialSelected)).perform(click());
        onData(allOf(is(instanceOf(SocialSituation.class)), is(SocialSituation.SEVERAL))).perform(click());

        // Set Reason field to "Testing Reason"
        onView(withId(R.id.reasonEdit)).perform(replaceText("Testing Reason"));

        // Confirm the edit by clicking the icon
        onView(withId(R.id.confirmEdit)).perform(click());

        // Sleep for async call to Firebase
        Thread.sleep(3000);

        // Click the topmost Mood Event
        onView(withId(R.id.mood_event_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //Verify the edits occurred
        onView(withId(R.id.moodDisplay)).check(matches(withText("Happy")));
        onView(withId(R.id.socialDisplay)).check(matches(withText("With several people")));
        onView(withId(R.id.reasonDisplay)).check(matches(withText("Testing Reason")));
        onView(withId(R.id.locationDisplay)).check(matches(withText("None")));
    }

}
