package com.nytimes.ui.dash.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import com.nytimes.R;
import com.nytimes.api.FeedDetailResponse;
import org.junit.*;

import static org.junit.Assert.assertEquals;

public class FeedDetailActivityTest {
      @Rule
    public ActivityTestRule<FeedDetailActivity> mActivityTestRule = new ActivityTestRule<>(
            FeedDetailActivity.class,
            true,    // initialTouchMode
            false);

    private FeedDetailActivity mActivity = null;

    private String mTitle = "Story";

    @Before
    public void setUp() throws Exception {
        Intent intent = new Intent();
        intent.putExtra("feedUrl", "Story");
        intent.putExtra("feedDate", "2019-03-29");
        mActivityTestRule.launchActivity(intent);
        mActivity = mActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

    @Test
    public void isActivityLaunch() {
        View view = mActivity.findViewById(R.id.tvTitle);
        Assert.assertNotNull(view);
    }


    @Test
    public void testSetTitleScenario() {
        FeedDetailResponse articleEntity = new FeedDetailResponse();
        articleEntity.setTitle(mTitle);
        assertEquals(articleEntity.getTitle(), mTitle);
    }

}