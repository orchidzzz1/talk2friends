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
public class createMeetingBlackBoxTest {
    @Rule
    public ActivityScenarioRule<createMeetingActivity> activityScenarioRule = new ActivityScenarioRule<>(createMeetingActivity.class);

    @Test
    public void create_emptyInputs() {
        // Perform an action on create meeting btn
        onView(withId(R.id.createNewMeetingButton)).perform(click());

        // Check if same view is displayed
        onView(withId(R.id.createMeetingActivity)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
    @Test
    public void create_partiallyEmptyInputs() {
        onView(withId(R.id.meetingNameText))
                .perform(ViewActions.typeText("Test"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.radioPhysical)).perform(click());
        onView(withId(R.id.editTextLocation))
                .perform(ViewActions.typeText("THH 201"), ViewActions.closeSoftKeyboard());
        // missing description

        // Perform action on create meeting btn
        onView(withId(R.id.createNewMeetingButton)).perform(click());

        // Check if same view is displayed since creating meeting failed
        onView(withId(R.id.createMeetingActivity)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
