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
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * This class contains intent tests for the Feed component of the app. It logs in with a test
 * account and then checks to see if the mood events of the users they follow are visible.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class FeedTest {
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
    public void testFriendActivityAppears() throws InterruptedException {
        loginWithTestAccount();
        onView(withId(R.id.navigation_feed)).perform(click());

        //Inelegant way to wait for network calls. Fix if there's time
        Thread.sleep(5000);

        onView(withText("personUserFollows1")).check(matches(isDisplayed()));
        onView(withText("ANGRY")).check(matches(isDisplayed()));
        onView(withText("personUserFollows2")).check(matches(isDisplayed()));
        onView(withText("SAD")).check(matches(isDisplayed()));
    }

}
