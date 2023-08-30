package com.sixtyninefourtwenty;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotEquals;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.ui.fragments.MiscFragment;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
class MiscFragmentTest {

    @Test
    void ponosQuoteRandomizerTest() {
        try (final var ignored = FragmentScenario.launchInContainer(MiscFragment.class, null, com.sixtyninefourtwenty.theming.R.style.AppTheme_Material2_Fallback)) {
            final CharSequence[] quotes = {null, null};
            onView(withId(R.id.ponos_quote)).check(matches(new TypeSafeMatcher<>() {

                @Override
                public void describeTo(Description description) {
                    description.appendText("text is null or empty");
                }

                @Override
                protected boolean matchesSafely(View item) {
                    final var text = ((TextView) item).getText();
                    quotes[0] = text;
                    return !TextUtils.isEmpty(text);
                }

            }));
            onView(withId(R.id.refresh_ponos_quote)).perform(click());
            onView(withId(R.id.ponos_quote)).check(matches(new TypeSafeMatcher<>() {

                @Override
                public void describeTo(Description description) {
                    description.appendText("text is null or empty");
                }

                @Override
                protected boolean matchesSafely(View item) {
                    final var text = ((TextView) item).getText();
                    quotes[1] = text;
                    return !TextUtils.isEmpty(text);
                }

            }));
            assertNotEquals(quotes[0], quotes[1]);
        }
    }

}
