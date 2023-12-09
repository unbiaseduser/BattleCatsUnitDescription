package com.sixtyninefourtwenty.bcud.ui.fragments.guidedetails.other;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;

import com.kennyc.view.MultiStateView;
import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.databinding.LoadingTextPageBinding;
import com.sixtyninefourtwenty.bcud.objects.Guide;
import com.sixtyninefourtwenty.bcud.utils.AssetMarkdownFileParsing;
import com.sixtyninefourtwenty.bcud.utils.Utils;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseViewBindingFragment;
import com.sixtyninefourtwenty.common.utils.MoreFutures;
import com.sixtyninefourtwenty.javastuff.AssetsJava;
import com.sixtyninefourtwenty.javastuff.concurrent.LifecycleAwareFutureContainer;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import lombok.AllArgsConstructor;

public final class GuideDetailsFragment extends BaseViewBindingFragment<@NonNull LoadingTextPageBinding> {

    @Override
    protected @NonNull LoadingTextPageBinding initBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return LoadingTextPageBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setup(@NonNull LoadingTextPageBinding binding, @Nullable Bundle savedInstanceState) {
        final var guide = GuideDetailsFragmentArgs.fromBundle(requireArguments()).getGuide();
        setToolbarTitle(guide.getTitle());
        final var container = new LifecycleAwareFutureContainer(getViewLifecycleOwner());
        final var markwon = guide.createMarkwon(requireContext(), binding.scroll);
        final var future = container.addAndReturn(AssetMarkdownFileParsing.parseFromObject(
                MyApplication.get(requireContext()).getThreadPool(),
                guide,
                requireContext(),
                markwon
        ));
        MoreFutures.addIgnoreExceptionsCallback(future,
                spanned -> {
                    markwon.setParsedMarkdown(binding.content, spanned);
                    Utils.applyBetterLinkMovementMethod(binding.content, this);
                    binding.getRoot().setViewState(MultiStateView.ViewState.CONTENT);
                },
                ContextCompat.getMainExecutor(requireContext()));
        requireActivity().addMenuProvider(new GuideDetailsFragmentMenu(guide), getViewLifecycleOwner());
    }

    @AllArgsConstructor
    private final class GuideDetailsFragmentMenu implements MenuProvider {
        private final Guide guide;

        @Override
        public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
            menuInflater.inflate(R.menu.menu_guide_other_alt, menu);
            final var combo = menu.findItem(R.id.go_to_combos);
            if (!guide.getUrl().equals("https://thanksfeanor.pythonanywhere.com/guides/documents/catcombos.html")) {
                combo.setVisible(false).setEnabled(false);
            } else {
                final var icon = AssetsJava.createDrawableFromAsset(requireContext().getAssets(), "img/combo_big.png");
                if (icon != null) {
                    combo.setIcon(icon);
                }
            }
            menu.findItem(R.id.text_search).setVisible(false).setEnabled(false);
        }

        @Override
        public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
            int id = menuItem.getItemId();
            if (id == R.id.guide_other_go_to_website) {
                openWebsite(guide.getUrl());
                return true;
            } else if (id == R.id.go_to_combos) {
                navigate(GuideDetailsFragmentDirections.actionNavGuideDetailToNavCombos());
                return true;
            }
            return false;
        }

    }

}