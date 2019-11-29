package com.cmput3owo1.moodlet.fragments;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.activities.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchTest {

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
    public void testSearchFragment() throws InterruptedException {
        loginWithTestAccount();

        // Navigate to Search fragment
        onView(withId(R.id.navigation_search)).perform(click());

        // Check if on search activity
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Discover"))));
    }

    @Test
    public void testSearchUser() throws InterruptedException {
        loginWithTestAccount();

        // Navigate to Search fragment
        onView(withId(R.id.navigation_search)).perform(click());

        // Enter in an existing user
        onView(withId(R.id.user_search_view)).perform(typeText("toad"));

        Thread.sleep(3000);

        // Check if user shows
        onView(withText("@toad")).check(matches(isDisplayed()));
    }
}
