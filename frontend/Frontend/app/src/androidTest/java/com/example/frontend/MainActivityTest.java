package com.example.frontend;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void open_home() {
        // Perform an action on create meeting btn
        onView(withId(R.id.navigation_home)).perform(click());

        // Check if same view is displayed
        onView(withId(R.id.fragmentHomePage)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void open_friendsPage() {
        // Perform an action on create meeting btn
        onView(withId(R.id.navigation_dashboard)).perform(click());

        // Check if same view is displayed
        onView(withId(R.id.friendsPageFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void open_profilePage() {
        // Perform an action on create meeting btn
        onView(withId(R.id.navigation_notifications)).perform(click());

        // Check if same view is displayed
        onView(withId(R.id.fragmentProfilePage)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }


}