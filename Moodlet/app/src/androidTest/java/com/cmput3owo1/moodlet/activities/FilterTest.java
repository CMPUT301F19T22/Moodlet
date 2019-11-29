package com.cmput3owo1.moodlet.activities;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.activities.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.cmput3owo1.moodlet.fragments.MoodHistoryFragmentTest.atPosition;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FilterTest {

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule(LoginActivity.class, false, true);

    // Start with MainActivity (JUnit Rules: https://developer.android.com/training/testing/junit-rules)
    private String testEmail = "test@ua.ca";
    private String testPassword = "123123";

    private void loginWithTestAccount() throws InterruptedException {
        // Logout first
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withText("Logout")).perform(click());

        Thread.sleep(5000);

        onView(withId(R.id.edit_text_email)).perform(typeText(testEmail));
        pressBack();
        onView(withId(R.id.edit_text_password)).perform(typeText(testPassword));
        pressBack();
        onView(withId(R.id.btn_login)).perform(click());

        //Inelegant way to wait for network calls. Fix if there's time
        Thread.sleep(5000);
    }
    @Test
    public void testFilterActivity() throws InterruptedException {
        loginWithTestAccount();

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
