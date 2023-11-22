package com.example.frontend;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

@RunWith(AndroidJUnit4.class)
public class verificationBLackBoxTest {

    @Rule
    public ActivityScenarioRule<VerificationPageActivity> activityScenarioRule = new ActivityScenarioRule<>(VerificationPageActivity.class);
    @Test
    public void create_emptyInputs() {
        // Perform an action on create meeting btn
        onView(withId(R.id.verifyButton)).perform(click());

        // Check if same view is displayed
        onView(withId(R.id.verificationPage)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
    @Test
    public void someTextInput() {
        onView(withId(R.id.verificationText))
                .perform(ViewActions.typeText("Test"), ViewActions.closeSoftKeyboard());

        // Perform an action on create meeting btn
        onView(withId(R.id.verifyButton)).perform(click());

        // Check if same view is displayed
        onView(withId(R.id.verificationPage)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }


}
