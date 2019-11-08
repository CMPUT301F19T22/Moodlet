package com.cmput3owo1.moodlet.fragments;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
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
 * This tests for proper user registration and registration for
 * already created accounts
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class RegisterUserTest {

    FirebaseFirestore db;
    FirebaseAuth auth;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void init(){
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @Test
    public void UserRegistrationTest() throws InterruptedException {

        fillInUserData(true,"Register Test", "register", "tester@gmail.com", "password", "password");

        ViewInteraction appCompatButton = onView(allOf(withId(R.id.btn_register), withText("Register")));
        appCompatButton.perform(click());

        ViewInteraction login_button = onView(allOf(withId(R.id.login_layout)));
        login_button.check(matches(isDisplayed()));

        Thread.sleep(3000);

        cleanUpTestData("register");

        Thread.sleep(3000);
    }


    @Test
    public void registerAllFieldsRequiredTest() throws InterruptedException {

        Thread.sleep(1500);

        ViewInteraction appCompatTextView = onView(allOf(withId(R.id.sign_up_text), withText(" Sign up")));
        appCompatTextView.perform(click());

        ViewInteraction appCompatButton = onView(allOf(withId(R.id.btn_register), withText("Register")));
        appCompatButton.perform(click());

        onView(withText(R.string.all_fields_required)).inRoot(withDecorView(not(mActivityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void registerPasswordsTooShortTest() throws InterruptedException {

        Thread.sleep(1500);

        fillInUserData(true,"test1", "test1", "test@test.com", "aa", "aa");

        ViewInteraction appCompatButton = onView(allOf(withId(R.id.btn_register), withText("Register")));
        appCompatButton.perform(click());

        onView(withText(R.string.password_too_short)).inRoot(withDecorView(not(mActivityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void registerPasswordsDoNotMatchTest() throws InterruptedException {

        Thread.sleep(1500);

        fillInUserData(true,"test1", "test1", "test@test.com", "Password123!", "not_the_same");

        ViewInteraction appCompatButton = onView(allOf(withId(R.id.btn_register), withText("Register")));
        appCompatButton.perform(click());

        onView(withText(R.string.password_does_not_match)).inRoot(withDecorView(not(mActivityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void UsernameAlreadyTakenRegistrationTest() throws InterruptedException {

        Thread.sleep(1500);

        fillInUserData(true,"test1", "test1", "test@test.com", "Password123!", "Password123!");

        ViewInteraction appCompatButton = onView(allOf(withId(R.id.btn_register), withText("Register")));
        appCompatButton.perform(click());

        onView(withText(R.string.username_taken)).inRoot(withDecorView(not(mActivityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));


    }

    @Test
    public void EmailAlreadyExistsRegistrationTest() throws InterruptedException {

        Thread.sleep(1500);

        fillInUserData(true,"test", "test_not_taken", "test@test.com", "Password123!", "Password123!");

        ViewInteraction appCompatButton = onView(allOf(withId(R.id.btn_register), withText("Register")));
        appCompatButton.perform(click());

        onView(withText(R.string.account_already_exists)).inRoot(withDecorView(not(mActivityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));

    }

    public void fillInUserData(boolean swapToSignUpScreen, String fullname, String username, String email, String password, String confirm_password){

        if(swapToSignUpScreen) {
            ViewInteraction appCompatTextView = onView(allOf(withId(R.id.sign_up_text), withText(" Sign up")));
            appCompatTextView.perform(click());
        }

        ViewInteraction appCompatEditText = onView(withId(R.id.full_name));
        appCompatEditText.perform(replaceText(fullname), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(withId(R.id.username));
        appCompatEditText2.perform(replaceText(username), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(withId(R.id.email));
        appCompatEditText3.perform(replaceText(email), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(withId(R.id.password));
        appCompatEditText4.perform(replaceText(password), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(withId(R.id.confirm_password));
        appCompatEditText5.perform(replaceText(confirm_password), closeSoftKeyboard());

    }


    public void cleanUpTestData(String username){
        db.collection("users").document(username).delete();
        auth.getCurrentUser().delete();
    }

}
