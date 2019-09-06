package com.nytimes.ui.dash.feeds;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import com.nytimes.R;
import org.junit.*;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class HomeActivityTest {
    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    private HomeActivity mActivity =null;

    @Before
    public void setUp() throws Exception {
       mActivity = mActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

    @Test
    public void isViewPresent() {
        View view = mActivity.findViewById(R.id.container);
        Assert.assertNotNull(view);
    }

    @Test
    public void onRecyclerViewClick() {
        Espresso.onView(withId(R.id.recycler)).perform(click());
    }
}