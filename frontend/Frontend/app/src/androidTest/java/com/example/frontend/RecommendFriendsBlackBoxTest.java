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
public class RecommendFriendsBlackBoxTest {

    @Rule
    public ActivityScenarioRule<RecommendPageActivity> activityScenarioRule = new ActivityScenarioRule<>(RecommendPageActivity.class);
    @Test
    public void returnFriendPageTest(){
        // Perform an action on create meeting btn
        onView(withId(R.id.returnToFriendsPage)).perform(click());

        // Check if same view is displayed
        onView(withId(R.id.friendsPageFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
