package com.cmput3owo1.moodlet.activities;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.cmput3owo1.moodlet.R;

import org.junit.Rule;
import org.junit.Test;

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

@LargeTest
public class RegisterErrorTests {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void registerAllFieldsRequiredTest(){

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatTextView = onView(allOf(withId(R.id.sign_up_text), withText(" Sign up")));
        appCompatTextView.perform(click());

        ViewInteraction appCompatButton = onView(allOf(withId(R.id.btn_register), withText("Register")));
        appCompatButton.perform(click());

        onView(withText(R.string.all_fields_required)).inRoot(withDecorView(not(mActivityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void registerPasswordsTooShortTest(){

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatTextView = onView(allOf(withId(R.id.sign_up_text), withText(" Sign up")));
        appCompatTextView.perform(click());

        ViewInteraction appCompatEditText = onView(withId(R.id.full_name));
        appCompatEditText.perform(replaceText("Register Test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(withId(R.id.username));
        appCompatEditText2.perform(replaceText("register"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(withId(R.id.email));
        appCompatEditText3.perform(replaceText("tester@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(withId(R.id.password));
        appCompatEditText4.perform(replaceText("aa"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(withId(R.id.confirm_password));
        appCompatEditText5.perform(replaceText("aa"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(allOf(withId(R.id.btn_register), withText("Register")));
        appCompatButton.perform(click());

        onView(withText(R.string.password_too_short)).inRoot(withDecorView(not(mActivityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void registerPasswordsDoNotMatchTest() {

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatTextView = onView(allOf(withId(R.id.sign_up_text), withText(" Sign up")));
        appCompatTextView.perform(click());

        ViewInteraction appCompatEditText = onView(withId(R.id.full_name));
        appCompatEditText.perform(replaceText("Register Test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(withId(R.id.username));
        appCompatEditText2.perform(replaceText("register"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(withId(R.id.email));
        appCompatEditText3.perform(replaceText("tester@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(withId(R.id.password));
        appCompatEditText4.perform(replaceText("password"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(withId(R.id.confirm_password));
        appCompatEditText5.perform(replaceText("not_the_same"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(allOf(withId(R.id.btn_register), withText("Register")));
        appCompatButton.perform(click());

        onView(withText(R.string.password_does_not_match)).inRoot(withDecorView(not(mActivityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }


}
