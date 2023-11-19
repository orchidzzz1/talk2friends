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
public class RegistrationBlackBoxTest {
    @Rule
    public ActivityScenarioRule<AuthenticationPageActivity> activityScenarioRule = new ActivityScenarioRule<>(AuthenticationPageActivity.class);

    @Test
    public void register_emptyInputs() {
        // Perform an action on register btn
        onView(withId(R.id.registerButton)).perform(click());

        // Check if same view is displayed
        onView(withId(R.id.AuthenticationPageActivity)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
    @Test
    public void register_partiallyEmptyInputs() {
        onView(withId(R.id.registerUsername))
                .perform(ViewActions.typeText("Test"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.musicCheckBox)).perform(click());

        // Perform action on register btn
        onView(withId(R.id.registerButton)).perform(click());

        // Check if same view is displayed since registration failed
        onView(withId(R.id.AuthenticationPageActivity)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
