package com.cmput3owo1.moodlet.fragments;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.filters.SdkSuppress;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

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
import static org.junit.Assert.*;

/**
 * This tests to see if the Google Map is loaded when navigating to the Map section
 */
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
@LargeTest
public class MapFragmentTest {

    /**
     * TEST ACCOUNT DESCRIPTION
     *
     * Map Test 1
     * email: maptest1@test.com
     * username: maptest1
     * password: password
     *
     * Mood Event:
     * Emotional State: Happy
     * Location: University of Alberta
     */

    /**
     * TEST ACCOUNT DESCRIPTION
     *
     * Map Test 2
     * email: maptest2@test.com
     * username: maptest2
     * password: password
     *
     * Mood Event:
     * Emotional State: Excited
     * Location: Electrical and Computer Engineering Research Facility
     */

    // Login with maptest1
    private String testEmail = "maptest1@test.com";
    private String testPassword = "password";

    // Start with LoginActivity (JUnit Rules: https://developer.android.com/training/testing/junit-rules)
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
    public void testMapFragmentDisplayed() throws InterruptedException {
        // Login
        loginWithTestAccount();

        // Navigate to the MapFragment
        onView(withId(R.id.navigation_map)).perform(click());

        // Check that the MapView is displayed
        onView(withId(R.id.mapView)).check(matches(isDisplayed()));
    }

    @Test
    public void testMoodEventDisplayed() throws UiObjectNotFoundException, InterruptedException {
        // Login
        loginWithTestAccount();

        // Navigate to the MapFragment
        onView(withId(R.id.navigation_map)).perform(click());

        // Get the marker
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("University of Alberta"));
        assertTrue(marker.exists());
        marker.click();
    }

    @Test
    public void testFriendMoodEventDisplayed() throws UiObjectNotFoundException, InterruptedException {
        // Login
        loginWithTestAccount();

        // Navigate to the MapFragment
        onView(withId(R.id.navigation_map)).perform(click());

        // Get the marker
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Electrical and Computer Engineering Research Facility"));
        assertFalse(marker.exists());

        onView(withId(R.id.showFollowers)).perform(click());

        marker = device.findObject(new UiSelector().descriptionContains("Electrical and Computer Engineering Research Facility"));
        assertTrue(marker.exists());
        marker.click();
    }
}
