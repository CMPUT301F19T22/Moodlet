package com.cmput3owo1.moodlet.activities;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.cmput3owo1.moodlet.R;
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
    public void initialUserRegistrationTest() throws InterruptedException {

        fillInUserData();

        ViewInteraction appCompatButton = onView(allOf(withId(R.id.btn_register), withText("Register")));
        appCompatButton.perform(click());

        ViewInteraction linearLayout = onView(allOf(withId(R.id.login_layout)));
        linearLayout.check(matches(isDisplayed()));

        Thread.sleep(2000);
    }

    @Test
    public void EmailAlreadyExistsRegistrationTest() throws InterruptedException {

        fillInUserData();

        ViewInteraction appCompatButton = onView(allOf(withId(R.id.btn_register), withText("Register")));
        appCompatButton.perform(click());

        Thread.sleep(1000);

        onView(withText(R.string.account_already_exists)).inRoot(withDecorView(not(mActivityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));

        Thread.sleep(1000);

        cleanUpTestData();

    }

    public void fillInUserData(){
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
        appCompatEditText5.perform(replaceText("password"), closeSoftKeyboard());

    }

    public void cleanUpTestData(){
        db.collection("users").document("register").delete();
        auth.getCurrentUser().delete();
    }
}
