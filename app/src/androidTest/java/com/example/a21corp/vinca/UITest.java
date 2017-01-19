package com.example.a21corp.vinca;

import android.content.ComponentName;
import android.os.SystemClock;
import android.support.test.espresso.action.TypeTextAction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.example.a21corp.vinca.Editor.EditorActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.os.SystemClock.currentThreadTimeMillis;
import static android.os.SystemClock.sleep;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class UITest {

    private String mStringToBeTyped;
    private String mOriginalWorkspaceName;
    private long currentTime;

    @Rule
    public IntentsTestRule<MainMenu> mActivityRule
            = new IntentsTestRule<>(MainMenu.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        currentTime = SystemClock.elapsedRealtimeNanos();
        mOriginalWorkspaceName = "UI" + currentTime;
        mStringToBeTyped = "Espresso" + currentTime;
    }

    @Test
    public void CreateNewProjectTest() {
        // Create a new Project
        onView(withId(R.id.newProjectButton))
                .perform(click());

        sleep(250);

        onView(withId(R.id.edittextname))
                .perform(new TypeTextAction(mOriginalWorkspaceName), closeSoftKeyboard());

        sleep(250);

        onView(withText("Create project"))
                .perform(click());

        // Ensure Editor has started
        //intended(hasComponent(new ComponentName(getTargetContext(), EditorActivity.class)));

        // Check that project is given correct name
        onView(withId(R.id.text_project_name))
                .check(matches(withText(mOriginalWorkspaceName)));
    }

    @Test
    public void LoadRandomProjectTest() {
        // Enter Load Menu
        onView(withId(R.id.loadMenuButton))
                .perform(click());

        // Ensure Editor has started
        intended(hasComponent(new ComponentName(getTargetContext(), LoadActivity.class)));

        // Load the first project in the list
        onData(anything())
                .inAdapterView(withContentDescription("loadList"))
                .atPosition(0)
                .perform(click());

        onView(withText("Load"))
                .perform(click());

        // Ensure Editor has started
        intended(hasComponent(new ComponentName(getTargetContext(), EditorActivity.class)));

        // Type text and then press the button.
        onView(withId(R.id.text_project_name))
                .perform(clearText())
                .perform(typeText(mStringToBeTyped), closeSoftKeyboard());
        onView(withId(R.id.text_project_name)).perform(click());

        // Check that the text was changed.
        onView(withId(R.id.text_project_name))
                .check(matches(withText(mStringToBeTyped)));
    }
}