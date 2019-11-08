package com.cmput3owo1.moodlet.fragments;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.activities.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * This tests for proper user login and failed user login with wrong
 * credentials and missing fields
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginUserTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void loginUserEmptyFieldsTest() throws InterruptedException {

        ViewInteraction appCompatButton = onView(allOf(withId(R.id.btn_login), withText("Login")));
        appCompatButton.perform(click());

        Thread.sleep(1000);

        onView(withText(R.string.all_fields_required)).inRoot(withDecorView(not(mActivityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));

    }

    @Test
    public void loginUserSuccessTest() throws InterruptedException {

        ViewInteraction appCompatEditText = onView(withId(R.id.edit_text_email));
        appCompatEditText.perform(replaceText("dtran1@ualberta.ca"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(withId(R.id.edit_text_password));
        appCompatEditText2.perform(replaceText("password"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(allOf(withId(R.id.btn_login), withText("Login")));
        appCompatButton.perform(click());

        Thread.sleep(1500);

        ViewInteraction frameLayout = onView(withId(R.id.nav_view));
        frameLayout.check(matches(isDisplayed()));
    }

    @Test
    public void loginUserFailTest() throws InterruptedException {

        ViewInteraction appCompatEditText = onView(withId(R.id.edit_text_email));
        appCompatEditText.perform(replaceText("dtran1@ualberta.ca"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(withId(R.id.edit_text_password));
        appCompatEditText2.perform(replaceText("wrong_password"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(allOf(withId(R.id.btn_login), withText("Login")));
        appCompatButton.perform(click());

        Thread.sleep(1000);

        onView(withText(R.string.authentication_failed)).inRoot(withDecorView(not(mActivityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));



    }

}
