package com.sixtyninefourtwenty.bcud.ui.fragments.guidedetails.advents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.kennyc.view.MultiStateView;
import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.databinding.LoadingTextPageBinding;
import com.sixtyninefourtwenty.bcud.utils.AppPreferences;
import com.sixtyninefourtwenty.bcud.utils.AssetMarkdownFileParsing;
import com.sixtyninefourtwenty.bcud.utils.Utils;
import com.sixtyninefourtwenty.bcud.utils.builders.MarkwonBuilderWrapper;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseFragment;
import com.sixtyninefourtwenty.bcud.viewmodels.AppSettingsViewModel;
import com.sixtyninefourtwenty.common.utils.MoreFutures;
import com.sixtyninefourtwenty.common.utils.TabLayoutViewPagerScreen;
import com.sixtyninefourtwenty.javastuff.concurrent.FutureContainer;
import com.sixtyninefourtwenty.javastuff.concurrent.LifecycleAwareFutureContainer;
import com.sixtyninefourtwenty.stuff.adapters.ListFragmentStateAdapter;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public final class GuideDetailsAdventFragment extends BaseFragment<@NonNull FrameLayout> {
    private AppSettingsViewModel settingsViewModel;
    private FutureContainer futureContainer;
    private final MenuProvider provider = new MenuProvider() {
        @Override
        public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
            menuInflater.inflate(R.menu.menu_advents_guide, menu);
            menu.findItem(R.id.text_search).setVisible(false).setEnabled(false);
        }

        @Override
        public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
            int id = menuItem.getItemId();
            if (id == R.id.advents_toggle_guide_view) {
                final var settingsModel = AppSettingsViewModel.get(requireActivity());
                final String format;
                if (settingsModel.getAdventViewMode().getValue() == AppPreferences.AdventViewMode.TABS) {
                    format = getString(R.string.full_doc_view);
                } else format = getString(R.string.tab_view);
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.toggle_advent_guide_view)
                        .setMessage(getString(R.string.prompt_toggle_guide_view, format))
                        .setPositiveButton(android.R.string.ok, (d, w) -> settingsModel.setAdventViewMode(settingsModel.getAdventViewMode().getValue() == AppPreferences.AdventViewMode.TEXT ? AppPreferences.AdventViewMode.TABS : AppPreferences.AdventViewMode.TEXT))
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
                return true;
            } else if (id == R.id.advents_go_to_website) {
                openWebsite("https://thanksfeanor.pythonanywhere.com/guides/documents/advents.html");
                return true;
            }
            return false;
        }

        @Override
        public void onPrepareMenu(@NonNull Menu menu) {
            final var toggle = menu.findItem(R.id.advents_toggle_guide_view);
            if (settingsViewModel.getAdventViewMode().getValue() == AppPreferences.AdventViewMode.TABS) {
                toggle.setIcon(R.drawable.description);
            } else toggle.setIcon(R.drawable.tab);
        }
    };

    @Override
    protected @NonNull FrameLayout initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return new FrameLayout(requireContext());
    }

    @Override
    protected void setup(@NonNull FrameLayout view, @Nullable Bundle savedInstanceState) {
        futureContainer = new LifecycleAwareFutureContainer(getViewLifecycleOwner());
        settingsViewModel = AppSettingsViewModel.get(requireActivity());
        settingsViewModel.getAdventViewMode().observe(getViewLifecycleOwner(), mode -> {
            if (mode == AppPreferences.AdventViewMode.TABS) {
                switchToTabs(view);
            } else {
                switchToFullDoc(view);
            }
            requireActivity().invalidateMenu();
        });
        requireActivity().addMenuProvider(provider, getViewLifecycleOwner());
    }

    @Override
    public void onPause() {
        super.onPause();
        settingsViewModel.persistAdventViewMode();
    }

    private void switchToTabs(FrameLayout root) {
        root.removeAllViews();
        final var screen = new TabLayoutViewPagerScreen(requireContext(), false,
                new ListFragmentStateAdapter(requireActivity(),
                        List.of(GuideDetailsAdventTabTextsFragment::new,
                                GuideDetailsAdventTabListFragment::new)),
                (tab, position) -> {
                    if (position == 0) tab.setText(R.string.tab_intro);
                    else tab.setText(R.string.tab_list_bosses);
                });
        root.addView(screen.getRoot());
    }

    private void switchToFullDoc(FrameLayout root) {
        futureContainer.close();
        root.removeAllViews();
        final var binding = LoadingTextPageBinding.inflate(getLayoutInflater(), root, false);
        root.addView(binding.getRoot());
        final var markwon = new MarkwonBuilderWrapper(requireContext())
                .images().html().anchor(binding.scroll).build();
        final var future = futureContainer.addAndReturn(AssetMarkdownFileParsing.parseArbitraryFile(
                MyApplication.get(requireContext()).getThreadPool(),
                requireContext(),
                "text/guides/advents_full.md",
                markwon
        ));
        MoreFutures.addIgnoreExceptionsCallback(future,
                spanned -> {
                    markwon.setParsedMarkdown(binding.content, spanned);
                    Utils.applyBetterLinkMovementMethod(binding.content, this);
                    binding.getRoot().setViewState(MultiStateView.ViewState.CONTENT);
                },
                ContextCompat.getMainExecutor(requireContext()));
    }

}
