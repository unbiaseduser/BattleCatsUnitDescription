package com.sixtyninefourtwenty;

import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.ui.fragments.MiscFragment;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MiscFragmentUnitTest {

    @Test
    public void ponosQuoteRandomizerTest() {
        final CharSequence[] quotes = {null, null};
        try (final var scenario = FragmentScenario.launchInContainer(MiscFragment.class, null, com.sixtyninefourtwenty.theming.R.style.AppTheme_Material2_Fallback)) {
            scenario.onFragment(fragment -> {
                final var rootView = fragment.requireView();
                final var quoteTextView = rootView.<TextView>findViewById(R.id.ponos_quote);
                final var refreshQuoteButton = rootView.<Button>findViewById(R.id.refresh_ponos_quote);
                Assert.assertFalse(quoteTextView.getText() == null || quoteTextView.getText().toString().isBlank());
                quotes[0] = quoteTextView.getText();
                refreshQuoteButton.performClick();
                Assert.assertFalse(quoteTextView.getText() == null || quoteTextView.getText().toString().isBlank());
                quotes[1] = quoteTextView.getText();
                Assert.assertNotEquals(quotes[0], quotes[1]);
            });
        }
    }

}
