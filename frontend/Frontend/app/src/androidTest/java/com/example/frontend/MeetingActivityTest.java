package com.example.frontend;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import org.junit.Before;
import org.junit.Test;

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
public class MeetingActivityTest {

    @Before
    public void setUp() {
        // Initialize your fragment scenario here if needed
        FragmentScenario.launchInContainer(MeetingActivity.class);
    }

    @Test
    public void testCreateMeetingBtn() {
        Espresso.onView(ViewMatchers.withId(R.id.goMeetingPageButton))
                .perform(ViewActions.click());

        onView(withId(R.id.createMeetingActivity)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    }
}
