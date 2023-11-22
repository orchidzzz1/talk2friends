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
public class MeetingBannerBlackBoxTest {
    @Rule
    public ActivityScenarioRule<MeetingBannerActivity> activityScenarioRule = new ActivityScenarioRule<>(MeetingBannerActivity.class);

    @Test
    public void testRSVPBtn() {
        onView(withId(R.id.btnRSVP)).perform(click());

        onView(withId(R.id.createMeetingActivity)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
