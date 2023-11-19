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
import com.example.frontend.LoginPageActivity;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;


@RunWith(AndroidJUnit4.class)
public class AuthBlackBoxTest {

    @Rule
    public ActivityScenarioRule<LoginPageActivity> activityScenarioRule = new ActivityScenarioRule<>(LoginPageActivity.class);

    @Test
    public void clickBtn_OpenGoogleLogin() {
        // Perform an action on google login btn when user is already logged in
        onView(withId(R.id.googleLoginButton)).perform(click());

        // Check if a new view is displayed
        onView(withId(R.id.container)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
